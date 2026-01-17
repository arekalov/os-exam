package com.arekalov.osexam.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import org.commonmark.node.BulletList
import org.commonmark.node.Code
import org.commonmark.node.FencedCodeBlock
import org.commonmark.node.HardLineBreak
import org.commonmark.node.Heading
import org.commonmark.node.Image
import org.commonmark.node.ListItem
import org.commonmark.node.Node
import org.commonmark.node.OrderedList
import org.commonmark.node.Paragraph
import org.commonmark.node.SoftLineBreak
import org.commonmark.node.StrongEmphasis
import org.commonmark.node.Text as MdText
import org.commonmark.node.Emphasis
import org.commonmark.node.ThematicBreak
import org.commonmark.parser.Parser
import androidx.compose.ui.tooling.preview.Preview
import com.arekalov.osexam.ui.theme.OsexamTheme

@Composable
fun MarkdownRenderer(
    markdown: String,
    onImageClick: (String) -> Unit
) {
    val processedMarkdown = remember(markdown) { preprocessLatex(markdown) }
    val blocks = remember(processedMarkdown) { parseMarkdown(processedMarkdown) }
    Column {
        blocks.forEach { block ->
            when (block) {
                is MdBlock.Heading -> {
                    val textStyle = when (block.level) {
                        1 -> MaterialTheme.typography.titleLarge
                        2 -> MaterialTheme.typography.titleMedium
                        3 -> MaterialTheme.typography.titleSmall
                        else -> MaterialTheme.typography.bodyMedium
                    }
                    Text(
                        text = buildInlineString(block.inlines),
                        style = textStyle,
                        fontWeight = FontWeight.Bold
                    )
                }
                is MdBlock.Divider -> {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
                is MdBlock.Paragraph -> {
                    Text(
                        text = buildInlineString(block.inlines),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                is MdBlock.BulletList -> {
                    block.items.forEach { item ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text(
                                text = "• ",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = buildInlineString(item),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                is MdBlock.OrderedList -> {
                    block.items.forEachIndexed { index, item ->
                        Row(modifier = Modifier.padding(vertical = 2.dp)) {
                            Text(
                                text = "${block.start + index}. ",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = buildInlineString(item),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                is MdBlock.Image -> {
                    AssetImage(
                        path = block.path,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clickable { onImageClick(block.path) }
                    )
                }
                is MdBlock.CodeBlock -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = block.text,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                is MdBlock.LatexBlock -> {
                    LatexFormula(
                        latex = block.latex,
                        isInline = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

private fun preprocessLatex(markdown: String): String {
    var processed = markdown
    
    // Replace \[ ... \] (display math) with $$...$$
    processed = processed.replace(Regex("""\\\["""), "$$")
    processed = processed.replace(Regex("""\\\]"""), "$$")
    
    // Replace block LaTeX $$...$$ with placeholder
    val blockLatexRegex = Regex("""\$\$([^\$]+?)\$\$""", RegexOption.DOT_MATCHES_ALL)
    val blockMatches = blockLatexRegex.findAll(processed).toList()
    blockMatches.reversed().forEach { match ->
        val placeholder = "%%%LATEX_BLOCK_${match.range.first}%%%"
        processed = processed.replaceRange(match.range, placeholder)
    }
    
    // Replace inline LaTeX $...$ with code syntax `...`
    val inlineLatexRegex = Regex("""\$([^\$\n]+)\$""")
    processed = inlineLatexRegex.replace(processed) { "`${it.groupValues[1]}`" }
    
    // Restore block LaTeX with custom marker
    blockMatches.forEach { match ->
        val placeholder = "%%%LATEX_BLOCK_${match.range.first}%%%"
        val latex = match.groupValues[1].trim()
        processed = processed.replace(placeholder, "\n%%%LATEXBLOCK%%%\n$latex\n%%%ENDLATEX%%%\n")
    }
    
    return processed
}

private fun parseMarkdown(markdown: String): List<MdBlock> {
    val parser = Parser.builder().build()
    val document = parser.parse(markdown)
    val blocks = mutableListOf<MdBlock>()
    var node: Node? = document.firstChild
    while (node != null) {
        when (node) {
            is Heading -> blocks.add(MdBlock.Heading(node.level, collectInlines(node)))
            is Paragraph -> {
                val text = extractTextFromNode(node)
                if (text.trim().startsWith("%%%LATEXBLOCK%%%")) {
                    val latex = text.replace("%%%LATEXBLOCK%%%", "").replace("%%%ENDLATEX%%%", "").trim()
                    blocks.add(MdBlock.LatexBlock(latex))
                } else {
                    blocks.addAll(blocksFromParagraph(node))
                }
            }
            is BulletList -> blocks.add(MdBlock.BulletList(collectListItems(node)))
            is OrderedList -> blocks.add(MdBlock.OrderedList(node.startNumber, collectListItems(node)))
            is FencedCodeBlock -> blocks.add(MdBlock.CodeBlock(node.literal))
            is ThematicBreak -> blocks.add(MdBlock.Divider)
        }
        node = node.next
    }
    return blocks
}

private fun extractTextFromNode(node: Node): String {
    val sb = StringBuilder()
    var child: Node? = node.firstChild
    while (child != null) {
        if (child is MdText) {
            sb.append(child.literal)
        }
        child = child.next
    }
    return sb.toString()
}

private fun blocksFromParagraph(paragraph: Paragraph): List<MdBlock> {
    val blocks = mutableListOf<MdBlock>()
    val buffer = mutableListOf<MdInline>()
    var child: Node? = paragraph.firstChild
    while (child != null) {
        if (child is Image) {
            if (buffer.isNotEmpty()) {
                blocks.add(MdBlock.Paragraph(buffer.toList()))
                buffer.clear()
            }
            blocks.add(MdBlock.Image(child.destination, child.title))
        } else {
            buffer.addAll(collectInlines(child))
        }
        child = child.next
    }
    if (buffer.isNotEmpty()) {
        blocks.add(MdBlock.Paragraph(buffer.toList()))
    }
    return blocks
}

private fun collectListItems(list: BulletList): List<List<MdInline>> {
    val items = mutableListOf<List<MdInline>>()
    var child: Node? = list.firstChild
    while (child != null) {
        if (child is ListItem) {
            val inlines = mutableListOf<MdInline>()
            var itemChild: Node? = child.firstChild
            while (itemChild != null) {
                if (itemChild is Paragraph) {
                    inlines.addAll(collectInlines(itemChild))
                }
                itemChild = itemChild.next
            }
            items.add(inlines)
        }
        child = child.next
    }
    return items
}

private fun collectListItems(list: OrderedList): List<List<MdInline>> {
    val items = mutableListOf<List<MdInline>>()
    var child: Node? = list.firstChild
    while (child != null) {
        if (child is ListItem) {
            val inlines = mutableListOf<MdInline>()
            var itemChild: Node? = child.firstChild
            while (itemChild != null) {
                if (itemChild is Paragraph) {
                    inlines.addAll(collectInlines(itemChild))
                }
                itemChild = itemChild.next
            }
            items.add(inlines)
        }
        child = child.next
    }
    return items
}

private fun collectInlines(node: Node): List<MdInline> {
    val inlines = mutableListOf<MdInline>()
    var child: Node? = node.firstChild
    if (child == null) {
        when (node) {
            is MdText -> inlines.add(MdInline.Text(node.literal))
            is SoftLineBreak -> inlines.add(MdInline.Text("\n"))
            is HardLineBreak -> inlines.add(MdInline.Text("\n"))
            is Code -> inlines.add(MdInline.Code(node.literal))
        }
        return inlines
    }
    while (child != null) {
        when (child) {
            is MdText -> inlines.add(MdInline.Text(child.literal))
            is Emphasis -> inlines.add(MdInline.Emphasis(collectInlines(child)))
            is StrongEmphasis -> inlines.add(MdInline.Strong(collectInlines(child)))
            is SoftLineBreak -> inlines.add(MdInline.Text("\n"))
            is HardLineBreak -> inlines.add(MdInline.Text("\n"))
            is Code -> inlines.add(MdInline.Code(child.literal))
        }
        child = child.next
    }
    return inlines
}

private fun buildInlineString(inlines: List<MdInline>): AnnotatedString = buildAnnotatedString {
    inlines.forEach { inline ->
        when (inline) {
            is MdInline.Text -> append(inline.text)
            is MdInline.Code -> withStyle(
                SpanStyle(fontFamily = FontFamily.Monospace)
            ) { append(inline.text) }
            is MdInline.Emphasis -> withStyle(
                SpanStyle(fontStyle = FontStyle.Italic)
            ) { append(buildInlineString(inline.children)) }
            is MdInline.Strong -> withStyle(
                SpanStyle(fontWeight = FontWeight.Bold)
            ) { append(buildInlineString(inline.children)) }
        }
    }
}

private sealed interface MdBlock {
    data class Heading(val level: Int, val inlines: List<MdInline>) : MdBlock
    data class Paragraph(val inlines: List<MdInline>) : MdBlock
    data class BulletList(val items: List<List<MdInline>>) : MdBlock
    data class OrderedList(val start: Int, val items: List<List<MdInline>>) : MdBlock
    data class Image(val path: String, val title: String?) : MdBlock
    data class CodeBlock(val text: String) : MdBlock
    data class LatexBlock(val latex: String) : MdBlock
    data object Divider : MdBlock
}

private sealed interface MdInline {
    data class Text(val text: String) : MdInline
    data class Emphasis(val children: List<MdInline>) : MdInline
    data class Strong(val children: List<MdInline>) : MdInline
    data class Code(val text: String) : MdInline
}

@Composable
private fun MarkdownPreviewContainer(content: @Composable () -> Unit) {
    OsexamTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(8.dp)) {
                content()
            }
        }
    }
}

@Preview(name = "Markdown Heading", device = "id:wearos_small_round")
@Composable
private fun PreviewMarkdownHeading() {
    MarkdownPreviewContainer {
        MarkdownRenderer(markdown = "# Heading title", onImageClick = {})
    }
}

@Preview(name = "Markdown Paragraph", device = "id:wearos_small_round")
@Composable
private fun PreviewMarkdownParagraph() {
    MarkdownPreviewContainer {
        MarkdownRenderer(
            markdown = "Paragraph text with *italic* and **bold**.",
            onImageClick = {}
        )
    }
}

@Preview(name = "Markdown Bullet List", device = "id:wearos_small_round")
@Composable
private fun PreviewMarkdownBulletList() {
    MarkdownPreviewContainer {
        MarkdownRenderer(
            markdown = "- First item\n- Second item\n- Third item",
            onImageClick = {}
        )
    }
}

@Preview(name = "Markdown Ordered List", device = "id:wearos_small_round")
@Composable
private fun PreviewMarkdownOrderedList() {
    MarkdownPreviewContainer {
        MarkdownRenderer(
            markdown = "1. First item\n2. Second item\n3. Third item",
            onImageClick = {}
        )
    }
}

@Preview(name = "Markdown Code Block", device = "id:wearos_small_round")
@Composable
private fun PreviewMarkdownCodeBlock() {
    MarkdownPreviewContainer {
        MarkdownRenderer(
            markdown = "```kotlin\nval x = 42\nprintln(x)\n```",
            onImageClick = {}
        )
    }
}

@Preview(name = "Markdown Image", device = "id:wearos_small_round")
@Composable
private fun PreviewMarkdownImage() {
    MarkdownPreviewContainer {
        MarkdownRenderer(
            markdown = "[Sample](images/process_states.png)",
            onImageClick = {}
        )
    }
}

@Preview(name = "Markdown Divider", device = "id:wearos_small_round")
@Composable
private fun PreviewMarkdownDivider() {
    MarkdownPreviewContainer {
        MarkdownRenderer(
            markdown = "Текст до разделителя\n\n---\n\nТекст после разделителя",
            onImageClick = {}
        )
    }
}

@Preview(name = "Markdown Heading Levels", device = "id:wearos_small_round")
@Composable
private fun PreviewMarkdownHeadingLevels() {
    MarkdownPreviewContainer {
        MarkdownRenderer(
            markdown = "# Заголовок 1\n## Заголовок 2\n### Заголовок 3\nОбычный текст",
            onImageClick = {}
        )
    }
}

@Preview(name = "Markdown Inline Formatting", device = "id:wearos_small_round")
@Composable
private fun PreviewMarkdownInlineFormatting() {
    MarkdownPreviewContainer {
        MarkdownRenderer(
            markdown = "Текст с *курсивом*, **жирным** и `кодом`.",
            onImageClick = {}
        )
    }
}

