package org.checkit.task.domain;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.client.util.DateTime;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import org.checkit.task.infrastructure.TaskRepository;
import org.checkit.user.domain.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleCalendarImportService {

    private final TaskRepository taskRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    private Calendar buildCalendarClient(OAuth2AuthenticationToken authToken) throws Exception {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authToken.getAuthorizedClientRegistrationId(),
                authToken.getName()
        );
        String tokenValue = client.getAccessToken().getTokenValue();
        GoogleCredentials credentials = GoogleCredentials.create(new AccessToken(tokenValue, null));

        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName("Checkit").build();
    }

    @Transactional
    public List<Task> importRange(User user, OAuth2AuthenticationToken authToken,
                                  LocalDate from, LocalDate to) throws Exception {
        Calendar calendar = buildCalendarClient(authToken);

        DateTime timeMin = new DateTime(from.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        DateTime timeMax = new DateTime(to.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());

        Events events = calendar.events().list("primary")
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
                .setSingleEvents(true)
                .setOrderBy("startTime")
                .execute();

        List<Task> imported = new ArrayList<>();

        for (Event event : events.getItems()) {
            if (taskRepository.existsByGoogleCalendarEventId(event.getId())) {
                continue;
            }

            Task task = mapEventToTask(event, user);
            imported.add(taskRepository.save(task));
        }

        return imported;
    }
    @Transactional
    public List<Task> importToday(User user, OAuth2AuthenticationToken authToken) throws Exception {
        LocalDate today = LocalDate.now();
        return importRange(user, authToken, today, today);
    }

    @Transactional
    public List<Task> importCurrentWeek(User user, OAuth2AuthenticationToken authToken) throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(java.time.DayOfWeek.MONDAY);
        LocalDate sunday = today.with(java.time.DayOfWeek.SUNDAY);
        return importRange(user, authToken, monday, sunday);
    }

    private Task mapEventToTask(Event event, User user) {
        Task task = new Task();
        task.setUser(user);
        task.setTitle(event.getSummary() != null ? event.getSummary() : "Evento sin titulo");
        task.setDescription(event.getDescription());
        task.setState(State.PENDIENTE);
        task.setGoogleCalendarEventId(event.getId());

        if (event.getEnd() != null) {
            if (event.getEnd().getDate() != null) {
                task.setDeadline(LocalDate.parse(event.getEnd().getDate().toString()));
            } else if (event.getEnd().getDateTime() != null) {
                task.setDeadline(LocalDate.ofInstant(
                        java.time.Instant.ofEpochMilli(event.getEnd().getDateTime().getValue()),
                        ZoneId.systemDefault()
                ));
            }
        }

        return task;
    }
}