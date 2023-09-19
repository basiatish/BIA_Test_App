package com.basiatish.domain.repositories

import com.basiatish.domain.common.Result

interface DocumentsRepository {

    suspend fun uploadDocuments(filesList: Array<String>): Result<String>

}