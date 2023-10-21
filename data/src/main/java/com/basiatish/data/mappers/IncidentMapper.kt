package com.basiatish.data.mappers

import com.basiatish.data.api.entities.IncidentRemote
import com.basiatish.domain.entities.Incident

class IncidentMapper {

    fun toUploadIncident(taskID: Int, text: String): IncidentRemote {
        return IncidentRemote(
            taskID = taskID,
            text = text
        )
    }

    fun toIncident(incidentRemote: IncidentRemote): Incident {
        return Incident(
            taskID = incidentRemote.taskID,
            text = incidentRemote.text
        )
    }

}