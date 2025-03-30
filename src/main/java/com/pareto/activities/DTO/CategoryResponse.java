package com.pareto.activities.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class CategoryResponse {
    private Map<String, List<String>> categories;
}
