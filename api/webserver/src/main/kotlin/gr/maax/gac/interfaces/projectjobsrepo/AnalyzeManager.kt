package gr.maax.gac.interfaces.projectjobsrepo

import java.util.*

class AnalyzeManager {

    private val analyzeCache = mutableListOf<AnalyzedCacheEntry>()

    fun analyzeProject(projectId: Int): ProjectAnalyzer.AnalyzeResult {
        val analyzedResult = ProjectAnalyzer(projectId).analyze()

        analyzeCache.add(
            AnalyzedCacheEntry(
                token = analyzedResult.submitDeleteToken,
                projectId = projectId,
                result = analyzedResult
            )
        )

        return analyzedResult
    }

    fun cleanProject(token: UUID, dryRun: Boolean = false) {
        val cacheEntry = analyzeCache.first { it.token == token }
        ProjectCleaner(cacheEntry.projectId).clean(cacheEntry.result, dryRun)
    }


    data class AnalyzedCacheEntry(
        val token: UUID,
        val projectId: Int,
        val result: ProjectAnalyzer.AnalyzeResult
    )


}