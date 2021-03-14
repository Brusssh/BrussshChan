package jp.brusssh.discord.bot

import com.google.gson.Gson
import java.io.File
import java.nio.charset.Charset

data class Config(val webPort: Int, val discordToken: String) {
    companion object {
        private val config: Config = parseConfig("config.json")

        fun get(): Config {
            return config
        }

        fun parseConfig(filePath: String): Config {
            val source = File(filePath).readText(Charset.forName("UTF-8"))
            return Gson().fromJson(source, Config::class.java)!!
        }
    }
}
