package io.musicorum.api.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Period(val value: String) {
    @SerialName("7DAY")
    SevenDays("7DAY"),
    @SerialName("1MONTH")
    OneMonth("1MONTH"),
    @SerialName("3MONTH")
    ThreeMonths("3MONTH"),
    @SerialName("6MONTH")
    SixMonths("6MONTH"),
    @SerialName("12MONTH")
    TwelveMonths("12MONTH"),
    @SerialName("OVERALL")
    Overall("OVERALL")
}