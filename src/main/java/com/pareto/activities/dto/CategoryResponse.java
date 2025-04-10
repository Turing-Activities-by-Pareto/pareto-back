package com.pareto.activities.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class CategoryResponse {
    private Map<String, List<String>> categories;
}
