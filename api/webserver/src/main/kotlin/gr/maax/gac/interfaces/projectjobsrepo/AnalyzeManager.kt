package gr.maax.gac.interfaces.projectjobsrepo

import java.util.*

class AnalyzeManager {

    private val analyzeCache = mutableListOf<AnalyzedCacheEntry>()

    fun analyzeProject(projectId: Int) {
        val analyzedResult = ProjectAnalyzer(projectId).analyze()

        analyzeCache.add(
            AnalyzedCacheEntry(
                token = analyzedResult.submitDeleteToken,
                projectId = projectId,
                result = analyzedResult
            )
        )
    }

    fun cleanProject(token: UUID) {

    }


    data class AnalyzedCacheEntry(
        val token: UUID,
        val projectId: Int,
        val result: ProjectAnalyzer.AnalyzeResult
    )


}