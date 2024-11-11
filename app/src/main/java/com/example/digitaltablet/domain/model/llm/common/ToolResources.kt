package com.example.digitaltablet.domain.model.llm.common

data class ToolResources(
    val code_interpreter: CodeInterpreter,
    val file_search: FileSearch
) {
    data class CodeInterpreter (
        val file_ids: List<String>
    )

    data class FileSearch(
        val vector_store_ids: List<String>
    )
}

