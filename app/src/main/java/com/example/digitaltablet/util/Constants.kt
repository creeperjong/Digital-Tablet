package com.example.digitaltablet.util

import android.os.Build
import androidx.annotation.RawRes
import com.example.digitaltablet.BuildConfig
import com.example.digitaltablet.R

object Constants {

    object Mqtt {

        const val BROKER_URL = "tcp://mqtt1.rcsl.online:1883"

        const val BROKER_NAME = "NTNU-MQTT-Server-1"

        const val USERNAME = "rcsl"

        const val PASSWORD = "rcslmqtt"

        object Topic {

            // TODO

        }

    }

    object LanguageModel {

        const val BASE_URL = "https://api.openai.com/v1/"

        val PROJECTS = mapOf(
            "AI-based Research" to BuildConfig.AI_BASED_RESEARCH,
            "Chinese Language" to BuildConfig.CHINESE_LANGUAGE,
            "Creativity with Chinese Language" to BuildConfig.CREATIVITY_WITH_CHINESE_LANGUAGE,
            "Kindergarten" to BuildConfig.KINDERGARTEN,
            "Making prompts invisible" to BuildConfig.MAKING_PROMPTS_INVISIBLE,
            "Order people care" to BuildConfig.ORDER_PEOPLE_CARE,
            "Robot Storyteller" to BuildConfig.ROBOT_STORYTELLER,
            "Social emotional learning" to BuildConfig.SOCIAL_EMOTIONAL_LEARNING,
            "STEM Education" to BuildConfig.STEM_EDUCATION,
            "Testing Agents" to BuildConfig.TESTING_AGENTS
        )
    }

    object Rcsl {

        const val BASE_URL = "http://api.rcsl.online:8887/"

    }

}