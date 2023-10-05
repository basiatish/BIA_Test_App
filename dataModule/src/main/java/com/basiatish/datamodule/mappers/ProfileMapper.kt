package com.basiatish.datamodule.mappers

import com.basiatish.datamodule.api.entities.ProfileRemote
import com.basiatish.domain.entities.Profile

class ProfileMapper {

    fun toProfile(profileRemote: ProfileRemote): Profile {
        return Profile(
            profileRemote.userName,
            profileRemote.userType,
            profileRemote.compNumber,
            profileRemote.phone,
            profileRemote.citizenship,
            profileRemote.carType,
            profileRemote.carNumber,
            profileRemote.photoUrl
        )
    }

}