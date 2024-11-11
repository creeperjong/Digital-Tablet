package com.example.digitaltablet.domain.model.llm

import com.example.digitaltablet.domain.model.llm.common.ToolResources

data class Assistant(
    val id: String,
    val `object`: String,
    val created_at: Int,
    val name: String? = null,
    val description: String? = null,
    val model: String,
    val instructions: String? = null,
    val tools: List<Any>,
    val tool_resources: ToolResources?,
    val metadata: Any,
    val temperature: Double? = null,
    val top_p: Double? = null,
    val response_format: Any
)

data class AssistantList(
    val `object`: String,
    val data: List<Assistant>
)