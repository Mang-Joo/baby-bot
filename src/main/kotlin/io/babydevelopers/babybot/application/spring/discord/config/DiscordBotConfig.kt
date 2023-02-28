package io.babydevelopers.babybot.application.spring.discord.config

import io.babydevelopers.babybot.application.spring.discord.listener.CommandStudyController
import io.babydevelopers.babybot.application.spring.discord.listener.MentionChatGPTController
import io.babydevelopers.babybot.domain.SlashCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity.playing
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordBotConfig(
    private val commandStudyController: CommandStudyController,
) {
    @Bean
    fun bot(
        @Value("\${discord.token}") token: String,
        @Value("\${discord.playing-message}") message: String,
        mentionChatGPTController: MentionChatGPTController,
    ): JDA = JDABuilder.createLight(token)
        .setActivity(playing(message))
        .setAutoReconnect(true)
        .addEventListeners(mentionChatGPTController)
        .addEventListeners(commandStudyController)
        .build()
        .also { jda ->
            SlashCommand.values()
                .forEach { jda.upsertCommand(it.data).queue() }
        }
}
