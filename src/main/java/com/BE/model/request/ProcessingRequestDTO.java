package com.BE.model.request;

import lombok.Data;

import java.util.List;

@Data
public class ProcessingRequestDTO {
   private List<ProcessingDTO> processingList;
}
