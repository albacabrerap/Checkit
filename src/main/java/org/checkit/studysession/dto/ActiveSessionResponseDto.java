package org.checkit.studysession.dto;

import lombok.Data;
import java.util.List;

@Data
public class ActiveSessionResponseDto {
    private Long sessionId;
    private boolean isBlockingActive;
    private List<String> restrictedUrls;
}