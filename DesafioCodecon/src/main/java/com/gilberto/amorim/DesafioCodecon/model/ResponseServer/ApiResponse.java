package com.gilberto.amorim.DesafioCodecon.model.ResponseServer;

import java.time.LocalDateTime;
import java.util.List;

public record ApiResponse<T>(LocalDateTime timestamp, long execution_time_ms, List<T> data) {
}
