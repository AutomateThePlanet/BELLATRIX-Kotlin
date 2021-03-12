package solutions.bellatrix.web.components

import layout.LayoutComponent
import org.openqa.selenium.Dimension
import org.openqa.selenium.Point
import solutions.bellatrix.web.services.JavaScriptService

class Viewport : LayoutComponent {
    override lateinit var location: Point
    override lateinit var size: Dimension

    init {
        val javaScriptService = JavaScriptService
        location = Point(0, 0)
        val viewportWidth = javaScriptService.execute("return Math.max(document.documentElement.clientWidth, window.innerWidth || 0);").toString().toInt()
        val viewportHeight = javaScriptService.execute("return Math.max(document.documentElement.clientHeight, window.innerHeight || 0);").toString().toInt()
        size = Dimension(viewportWidth, viewportHeight)
    }

    override val elementName: String
        get() = "Screen"
}