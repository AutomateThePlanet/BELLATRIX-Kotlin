package solutions.bellatrix.web.components

import layout.LayoutComponent

class SpecialComponents {
    fun getViewport(): LayoutComponent {
        return Viewport()
    }

    fun getScreen(): LayoutComponent {
        return Screen()
    }
}