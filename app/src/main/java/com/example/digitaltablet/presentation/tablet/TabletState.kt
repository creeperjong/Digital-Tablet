package com.example.digitaltablet.presentation.tablet

import androidx.compose.ui.geometry.Offset

data class TabletState(

    // UI
    val toastMessages: List<String> = emptyList(),
    val displayTouchArea: Boolean = false,
    val canvasTapPositions: List<Offset> = emptyList(),
    val isCanvasTappable: Boolean = true,
    val isCaptionVisible: Boolean = true,
    val isImageVisible: Boolean = true,
    val showTextDialog: Boolean = false,
    val dialogTextInput: String = "",

    // R&T
    val deviceId: String = "",
    val caption: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean ultrices consequat nisl non bibendum. Sed bibendum laoreet dui, eu eleifend erat ultrices quis. Integer ut mi vestibulum, vehicula libero a, interdum augue. Donec egestas, tortor vitae consectetur efficitur, diam sem lobortis leo, quis eleifend sapien mi ut lorem. Sed aliquam pulvinar neque, nec pellentesque sem lacinia sit amet. Vivamus laoreet pellentesque nulla, at egestas tortor tempus vitae. Suspendisse posuere id tortor gravida fermentum. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque nunc arcu, semper in tortor sed, venenatis ultrices risus. Nulla facilisi. Cras molestie, sem at convallis mollis, eros leo gravida odio, at fringilla nibh sapien et nisl. Nullam interdum aliquet libero vel dapibus.\n" +
            "\n" +
            "In porttitor, orci non ultricies pellentesque, eros magna ultrices eros, venenatis pellentesque dui diam nec ex. Vivamus vitae dolor dapibus, hendrerit neque nec, tempus velit. Donec convallis, nisi vel lobortis porttitor, eros orci scelerisque erat, ac consectetur augue libero non mauris. Proin egestas mollis venenatis. Nullam auctor gravida orci quis tempor. Integer et vestibulum quam, ac dignissim nisi. Vestibulum consectetur turpis vel fermentum dignissim. Quisque vel euismod dui, in dictum sem. Nunc et ornare magna. Nulla feugiat vulputate nisi a vehicula. Nunc eget felis eget urna tincidunt suscipit. Donec posuere venenatis lorem. Maecenas tellus erat, auctor eu posuere sit amet, bibendum vel purus. Curabitur placerat lobortis felis. Sed vitae mi vel lacus pharetra condimentum vitae id diam. Ut ut gravida odio.\n" +
            "\n" +
            "Sed at nisl in eros consequat ultricies. Fusce ligula dui, bibendum eu tincidunt sed, eleifend at erat. Maecenas venenatis turpis augue, et sollicitudin massa ullamcorper ac. Vestibulum efficitur accumsan sapien ut aliquam. Curabitur dictum pulvinar libero. Cras massa massa, commodo a molestie id, interdum sit amet elit. Fusce eu tortor ultrices, lacinia nisi sit amet, vulputate felis. Morbi euismod, eros vel cursus ornare, urna dolor commodo urna, non aliquam velit metus quis mauris. In sed leo ac est ornare aliquam. In hac habitasse platea dictumst. Donec nec gravida nisi. Quisque condimentum vehicula urna in ultrices. Donec sagittis, ex feugiat aliquam consectetur, erat tellus efficitur libero, nec cursus nisi felis ac dolor. Morbi orci augue, facilisis vitae eleifend non, dignissim eu dolor. Mauris fringilla sem ut ex placerat, eu fermentum tortor laoreet.\n" +
            "\n" +
            "Quisque efficitur urna id magna luctus dapibus. Aliquam faucibus velit nec suscipit vehicula. Fusce commodo, tellus non suscipit aliquam, felis est tincidunt nibh, non scelerisque nunc turpis id lorem. Maecenas faucibus, orci quis ullamcorper semper, augue velit auctor dui, at tincidunt tortor ipsum auctor ex. Aliquam volutpat ornare enim, a semper leo viverra et. Praesent id mi vitae diam dignissim molestie non eu enim. Integer varius et mi sed tincidunt. Nullam quis est quis risus consequat hendrerit vitae non neque.\n" +
            "\n" +
            "Vestibulum hendrerit mattis est. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nam ut nisl sit amet metus hendrerit blandit nec in diam. Praesent consectetur, enim ac vulputate volutpat, turpis neque luctus sem, a aliquam est lacus laoreet nunc. Mauris tincidunt massa at sapien posuere, in luctus massa gravida. Sed dictum sit amet lectus at vestibulum. Duis sit amet fringilla nisi. In id turpis lobortis, elementum urna ac, blandit mi.",
    val responseCaption: String = "Learner: test response",
    val imageSources: List<String> = listOf("https://zidian.18dao.net/image/%E6%9D%AF.png", "https://zidian.18dao.net/image/%E7%93%B6.png"),
    val imageIdx: Int? = 0,

    // LLM
    val gptApiKey: String = "",
    val assistantId: String = ""
)