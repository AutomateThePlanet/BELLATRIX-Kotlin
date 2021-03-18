package solutions.bellatrix.web.components

import layout.LayoutComponent
import org.openqa.selenium.Dimension
import org.openqa.selenium.Point
import solutions.bellatrix.web.services.BrowserService

class Screen : LayoutComponent {
    override lateinit var location: Point
    override lateinit var size: Dimension

    init {
        val browserService = BrowserService
        location = Point(0, 0)
        size = browserService.wrappedDriver.manage().window().size
    }

    override val elementName: String
        get() = "Screen"
}