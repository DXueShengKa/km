package element_external

import androidx.compose.runtime.Composable
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.ContentBuilder
import org.jetbrains.compose.web.dom.ElementBuilder
import org.jetbrains.compose.web.dom.TagElement
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement


private class ElementBuilderImplementation<TElement : Element>(private val tagName: String) : ElementBuilder<TElement> {
    private val el: Element by lazy { document.createElement(tagName) }
    override fun create(): TElement = el.cloneNode() as TElement
}

private val Dl: ElementBuilder<HTMLElement> = ElementBuilderImplementation("dl")

private val DD: ElementBuilder<HTMLElement> = ElementBuilderImplementation("dd")

private val Dt: ElementBuilder<HTMLElement> = ElementBuilderImplementation("dt")

@Composable
fun Dl(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>? = null
){
    TagElement(elementBuilder = Dl, applyAttrs = attrs, content = content)
}

@Composable
fun Dt(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>? = null
){
    TagElement(elementBuilder = Dt, applyAttrs = attrs, content = content)
}

@Composable
fun DD(
    attrs: AttrBuilderContext<HTMLElement>? = null,
    content: ContentBuilder<HTMLElement>? = null
){
    TagElement(elementBuilder = DD, applyAttrs = attrs, content = content)
}
