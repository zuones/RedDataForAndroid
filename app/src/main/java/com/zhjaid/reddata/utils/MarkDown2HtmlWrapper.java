//package com.zhjaid.reddata.utils;
//
//import com.vladsch.flexmark.ast.Node;
//import com.vladsch.flexmark.ext.tables.TablesExtension;
//import com.vladsch.flexmark.html.HtmlRenderer;
//import com.vladsch.flexmark.parser.Parser;
//import com.vladsch.flexmark.parser.ParserEmulationProfile;
//import com.vladsch.flexmark.util.options.MutableDataSet;
//
//import java.io.IOException;
//import java.util.Arrays;
//
//public class MarkDown2HtmlWrapper {
//
//    private static String MD_CSS = null;
//
//    static {
//        try {
//            MD_CSS = "html {overflow-x: initial !important;}:root { --bg-color:#ffffff; --text-color:#333333; --select-text-bg-color:#B5D6FC; --select-text-font-color:auto; --monospace:\"Lucida Console\",Consolas,\"Courier\",monospace; --title-bar-height:20px; }\n" +
//                    ".mac-os-11 { --title-bar-height:28px; }\n" +
//                    "html { font-size: 14px; background-color: var(--bg-color); color: var(--text-color); font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif; -webkit-font-smoothing: antialiased; }\n" +
//                    "body { margin: 0px; padding: 0px; height: auto; bottom: 0px; top: 0px; left: 0px; right: 0px; font-size: 1rem; line-height: 1.42857; overflow-x: hidden; background: inherit; tab-size: 4; }\n" +
//                    "iframe { margin: auto; }\n" +
//                    "a.url { word-break: break-all; }\n" +
//                    "a:active, a:hover { outline: 0px; }\n" +
//                    ".in-text-selection, ::selection { text-shadow: none; background: var(--select-text-bg-color); color: var(--select-text-font-color); }\n" +
//                    "#write { margin: 0px auto; height: auto; width: inherit; word-break: normal; overflow-wrap: break-word; position: relative; white-space: normal; overflow-x: visible; padding-top: 36px; }\n" +
//                    "#write.first-line-indent p { text-indent: 2em; }\n" +
//                    "#write.first-line-indent li p, #write.first-line-indent p * { text-indent: 0px; }\n" +
//                    "#write.first-line-indent li { margin-left: 2em; }\n" +
//                    ".for-image #write { padding-left: 8px; padding-right: 8px; }\n" +
//                    "body.typora-export { padding-left: 30px; padding-right: 30px; }\n" +
//                    ".typora-export .footnote-line, .typora-export li, .typora-export p { white-space: pre-wrap; }\n" +
//                    ".typora-export .task-list-item input { pointer-events: none; }\n" +
//                    "@media screen and (max-width: 500px) {\n" +
//                    "  body.typora-export { padding-left: 0px; padding-right: 0px; }\n" +
//                    "  #write { padding-left: 20px; padding-right: 20px; }\n" +
//                    "  .CodeMirror-sizer { margin-left: 0px !important; }\n" +
//                    "  .CodeMirror-gutters { display: none !important; }\n" +
//                    "}\n" +
//                    "#write li > figure:last-child { margin-bottom: 0.5rem; }\n" +
//                    "#write ol, #write ul { position: relative; }\n" +
//                    "img { max-width: 100%; vertical-align: middle; image-orientation: from-image; }\n" +
//                    "button, input, select, textarea { color: inherit; font: inherit; }\n" +
//                    "input[type=\"checkbox\"], input[type=\"radio\"] { line-height: normal; padding: 0px; }\n" +
//                    "*, ::after, ::before { box-sizing: border-box; }\n" +
//                    "#write h1, #write h2, #write h3, #write h4, #write h5, #write h6, #write p, #write pre { width: inherit; }\n" +
//                    "#write h1, #write h2, #write h3, #write h4, #write h5, #write h6, #write p { position: relative; }\n" +
//                    "p { line-height: inherit; }\n" +
//                    "h1, h2, h3, h4, h5, h6 { break-after: avoid-page; break-inside: avoid; orphans: 4; }\n" +
//                    "p { orphans: 4; }\n" +
//                    "h1 { font-size: 2rem; }\n" +
//                    "h2 { font-size: 1.8rem; }\n" +
//                    "h3 { font-size: 1.6rem; }\n" +
//                    "h4 { font-size: 1.4rem; }\n" +
//                    "h5 { font-size: 1.2rem; }\n" +
//                    "h6 { font-size: 1rem; }\n" +
//                    ".md-math-block, .md-rawblock, h1, h2, h3, h4, h5, h6, p { margin-top: 1rem; margin-bottom: 1rem; }\n" +
//                    ".hidden { display: none; }\n" +
//                    ".md-blockmeta { color: rgb(204, 204, 204); font-weight: 700; font-style: italic; }\n" +
//                    "a { cursor: pointer; }\n" +
//                    "sup.md-footnote { padding: 2px 4px; background-color: rgba(238, 238, 238, 0.7); color: rgb(85, 85, 85); border-radius: 4px; cursor: pointer; }\n" +
//                    "sup.md-footnote a, sup.md-footnote a:hover { color: inherit; text-transform: inherit; text-decoration: inherit; }\n" +
//                    "#write input[type=\"checkbox\"] { cursor: pointer; width: inherit; height: inherit; }\n" +
//                    "figure { overflow-x: auto; margin: 1.2em 0px; max-width: calc(100% + 16px); padding: 0px; }\n" +
//                    "figure > table { margin: 0px; }\n" +
//                    "tr { break-inside: avoid; break-after: auto; }\n" +
//                    "thead { display: table-header-group; }\n" +
//                    "table { border-collapse: collapse; border-spacing: 0px; width: 100%; overflow: auto; break-inside: auto; text-align: left; }\n" +
//                    "table.md-table td { min-width: 32px; }\n" +
//                    ".CodeMirror-gutters { border-right: 0px; background-color: inherit; }\n" +
//                    ".CodeMirror-linenumber { user-select: none; }\n" +
//                    ".CodeMirror { text-align: left; }\n" +
//                    ".CodeMirror-placeholder { opacity: 0.3; }\n" +
//                    ".CodeMirror pre { padding: 0px 4px; }\n" +
//                    ".CodeMirror-lines { padding: 0px; }\n" +
//                    "div.hr:focus { cursor: none; }\n" +
//                    "#write pre { white-space: pre-wrap; }\n" +
//                    "#write.fences-no-line-wrapping pre { white-space: pre; }\n" +
//                    "#write pre.ty-contain-cm { white-space: normal; }\n" +
//                    ".CodeMirror-gutters { margin-right: 4px; }\n" +
//                    ".md-fences { font-size: 0.9rem; display: block; break-inside: avoid; text-align: left; overflow: visible; white-space: pre; background: inherit; position: relative !important; }\n" +
//                    ".md-fences-adv-panel { width: 100%; margin-top: 10px; text-align: center; padding-top: 0px; padding-bottom: 8px; overflow-x: auto; }\n" +
//                    "#write .md-fences.mock-cm { white-space: pre-wrap; }\n" +
//                    ".md-fences.md-fences-with-lineno { padding-left: 0px; }\n" +
//                    "#write.fences-no-line-wrapping .md-fences.mock-cm { white-space: pre; overflow-x: auto; }\n" +
//                    ".md-fences.mock-cm.md-fences-with-lineno { padding-left: 8px; }\n" +
//                    ".CodeMirror-line, twitterwidget { break-inside: avoid; }\n" +
//                    ".footnotes { opacity: 0.8; font-size: 0.9rem; margin-top: 1em; margin-bottom: 1em; }\n" +
//                    ".footnotes + .footnotes { margin-top: 0px; }\n" +
//                    ".md-reset { margin: 0px; padding: 0px; border: 0px; outline: 0px; vertical-align: top; background: 0px 0px; text-decoration: none; text-shadow: none; float: none; position: static; width: auto; height: auto; white-space: nowrap; cursor: inherit; -webkit-tap-highlight-color: transparent; line-height: normal; font-weight: 400; text-align: left; box-sizing: content-box; direction: ltr; }\n" +
//                    "li div { padding-top: 0px; }\n" +
//                    "blockquote { margin: 1rem 0px; }\n" +
//                    "li .mathjax-block, li p { margin: 0.5rem 0px; }\n" +
//                    "li blockquote { margin: 1rem 0px; }\n" +
//                    "li { margin: 0px; position: relative; }\n" +
//                    "blockquote > :last-child { margin-bottom: 0px; }\n" +
//                    "blockquote > :first-child, li > :first-child { margin-top: 0px; }\n" +
//                    ".footnotes-area { color: rgb(136, 136, 136); margin-top: 0.714rem; padding-bottom: 0.143rem; white-space: normal; }\n" +
//                    "#write .footnote-line { white-space: pre-wrap; }\n" +
//                    "@media print {\n" +
//                    "  body, html { border: 1px solid transparent; height: 99%; break-after: avoid; break-before: avoid; font-variant-ligatures: no-common-ligatures; }\n" +
//                    "  #write { margin-top: 0px; padding-top: 0px; border-color: transparent !important; }\n" +
//                    "  .typora-export * { -webkit-print-color-adjust: exact; }\n" +
//                    "  .typora-export #write { break-after: avoid; }\n" +
//                    "  .typora-export #write::after { height: 0px; }\n" +
//                    "  .is-mac table { break-inside: avoid; }\n" +
//                    "  .typora-export-show-outline .typora-export-sidebar { display: none; }\n" +
//                    "}\n" +
//                    ".footnote-line { margin-top: 0.714em; font-size: 0.7em; }\n" +
//                    "a img, img a { cursor: pointer; }\n" +
//                    "pre.md-meta-block { font-size: 0.8rem; min-height: 0.8rem; white-space: pre-wrap; background: rgb(204, 204, 204); display: block; overflow-x: hidden; }\n" +
//                    "p > .md-image:only-child:not(.md-img-error) img, p > img:only-child { display: block; margin: auto; }\n" +
//                    "#write.first-line-indent p > .md-image:only-child:not(.md-img-error) img { left: -2em; position: relative; }\n" +
//                    "p > .md-image:only-child { display: inline-block; width: 100%; }\n" +
//                    "#write .MathJax_Display { margin: 0.8em 0px 0px; }\n" +
//                    ".md-math-block { width: 100%; }\n" +
//                    ".md-math-block:not(:empty)::after { display: none; }\n" +
//                    ".MathJax_ref { fill: currentcolor; }\n" +
//                    "[contenteditable=\"true\"]:active, [contenteditable=\"true\"]:focus, [contenteditable=\"false\"]:active, [contenteditable=\"false\"]:focus { outline: 0px; box-shadow: none; }\n" +
//                    ".md-task-list-item { position: relative; list-style-type: none; }\n" +
//                    ".task-list-item.md-task-list-item { padding-left: 0px; }\n" +
//                    ".md-task-list-item > input { position: absolute; top: 0px; left: 0px; margin-left: -1.2em; margin-top: calc(1em - 10px); border: none; }\n" +
//                    ".math { font-size: 1rem; }\n" +
//                    ".md-toc { min-height: 3.58rem; position: relative; font-size: 0.9rem; border-radius: 10px; }\n" +
//                    ".md-toc-content { position: relative; margin-left: 0px; }\n" +
//                    ".md-toc-content::after, .md-toc::after { display: none; }\n" +
//                    ".md-toc-item { display: block; color: rgb(65, 131, 196); }\n" +
//                    ".md-toc-item a { text-decoration: none; }\n" +
//                    ".md-toc-inner:hover { text-decoration: underline; }\n" +
//                    ".md-toc-inner { display: inline-block; cursor: pointer; }\n" +
//                    ".md-toc-h1 .md-toc-inner { margin-left: 0px; font-weight: 700; }\n" +
//                    ".md-toc-h2 .md-toc-inner { margin-left: 2em; }\n" +
//                    ".md-toc-h3 .md-toc-inner { margin-left: 4em; }\n" +
//                    ".md-toc-h4 .md-toc-inner { margin-left: 6em; }\n" +
//                    ".md-toc-h5 .md-toc-inner { margin-left: 8em; }\n" +
//                    ".md-toc-h6 .md-toc-inner { margin-left: 10em; }\n" +
//                    "@media screen and (max-width: 48em) {\n" +
//                    "  .md-toc-h3 .md-toc-inner { margin-left: 3.5em; }\n" +
//                    "  .md-toc-h4 .md-toc-inner { margin-left: 5em; }\n" +
//                    "  .md-toc-h5 .md-toc-inner { margin-left: 6.5em; }\n" +
//                    "  .md-toc-h6 .md-toc-inner { margin-left: 8em; }\n" +
//                    "}\n" +
//                    "a.md-toc-inner { font-size: inherit; font-style: inherit; font-weight: inherit; line-height: inherit; }\n" +
//                    ".footnote-line a:not(.reversefootnote) { color: inherit; }\n" +
//                    ".md-attr { display: none; }\n" +
//                    ".md-fn-count::after { content: \".\"; }\n" +
//                    "code, pre, samp, tt { font-family: var(--monospace); }\n" +
//                    "kbd { margin: 0px 0.1em; padding: 0.1em 0.6em; font-size: 0.8em; color: rgb(36, 39, 41); background: rgb(255, 255, 255); border: 1px solid rgb(173, 179, 185); border-radius: 3px; box-shadow: rgba(12, 13, 14, 0.2) 0px 1px 0px, rgb(255, 255, 255) 0px 0px 0px 2px inset; white-space: nowrap; vertical-align: middle; }\n" +
//                    ".md-comment { color: rgb(162, 127, 3); opacity: 0.8; font-family: var(--monospace); }\n" +
//                    "code { text-align: left; vertical-align: initial; }\n" +
//                    "a.md-print-anchor { white-space: pre !important; border-width: initial !important; border-style: none !important; border-color: initial !important; display: inline-block !important; position: absolute !important; width: 1px !important; right: 0px !important; outline: 0px !important; background: 0px 0px !important; text-decoration: initial !important; text-shadow: initial !important; }\n" +
//                    ".md-inline-math .MathJax_SVG .noError { display: none !important; }\n" +
//                    ".html-for-mac .inline-math-svg .MathJax_SVG { vertical-align: 0.2px; }\n" +
//                    ".md-fences-math .MathJax_SVG_Display, .md-math-block .MathJax_SVG_Display { text-align: center; margin: 0px; position: relative; text-indent: 0px; max-width: none; max-height: none; min-height: 0px; min-width: 100%; width: auto; overflow-y: visible; display: block !important; }\n" +
//                    ".MathJax_SVG_Display, .md-inline-math .MathJax_SVG_Display { width: auto; margin: inherit; display: inline-block !important; }\n" +
//                    ".MathJax_SVG .MJX-monospace { font-family: var(--monospace); }\n" +
//                    ".MathJax_SVG .MJX-sans-serif { font-family: sans-serif; }\n" +
//                    ".MathJax_SVG { display: inline; font-style: normal; font-weight: 400; line-height: normal; text-indent: 0px; text-align: left; text-transform: none; letter-spacing: normal; word-spacing: normal; overflow-wrap: normal; white-space: nowrap; float: none; direction: ltr; max-width: none; max-height: none; min-width: 0px; min-height: 0px; border: 0px; padding: 0px; margin: 0px; zoom: 90%; }\n" +
//                    "#math-inline-preview-content { zoom: 1.1; }\n" +
//                    ".MathJax_SVG * { transition: none 0s ease 0s; }\n" +
//                    ".MathJax_SVG_Display svg { vertical-align: middle !important; margin-bottom: 0px !important; margin-top: 0px !important; }\n" +
//                    ".os-windows.monocolor-emoji .md-emoji { font-family: \"Segoe UI Symbol\", sans-serif; }\n" +
//                    ".md-diagram-panel > svg { max-width: 100%; }\n" +
//                    "[lang=\"flow\"] svg, [lang=\"mermaid\"] svg { max-width: 100%; height: auto; }\n" +
//                    "[lang=\"mermaid\"] .node text { font-size: 1rem; }\n" +
//                    "table tr th { border-bottom: 0px; }\n" +
//                    "video { max-width: 100%; display: block; margin: 0px auto; }\n" +
//                    "iframe { max-width: 100%; width: 100%; border: none; }\n" +
//                    ".highlight td, .highlight tr { border: 0px; }\n" +
//                    "mark { background: rgb(255, 255, 0); color: rgb(0, 0, 0); }\n" +
//                    ".md-html-inline .md-plain, .md-html-inline strong, mark .md-inline-math, mark strong { color: inherit; }\n" +
//                    ".md-expand mark .md-meta { opacity: 0.3 !important; }\n" +
//                    "mark .md-meta { color: rgb(0, 0, 0); }\n" +
//                    "@media print {\n" +
//                    "  .typora-export h1, .typora-export h2, .typora-export h3, .typora-export h4, .typora-export h5, .typora-export h6 { break-inside: avoid; }\n" +
//                    "}\n" +
//                    ".md-diagram-panel .messageText { stroke: none !important; }\n" +
//                    ".md-diagram-panel .start-state { fill: var(--node-fill); }\n" +
//                    ".md-diagram-panel .edgeLabel rect { opacity: 1 !important; }\n" +
//                    ".md-require-zoom-fix foreignobject { font-size: var(--mermaid-font-zoom); }\n" +
//                    ".md-fences.md-fences-math { font-size: 1em; }\n" +
//                    ".md-fences-math .MathJax_SVG_Display { margin-top: 8px; cursor: default; }\n" +
//                    ".md-fences-advanced:not(.md-focus) { padding: 0px; white-space: nowrap; border: 0px; }\n" +
//                    ".md-fences-advanced:not(.md-focus) { background: inherit; }\n" +
//                    ".typora-export-show-outline .typora-export-content { max-width: 1440px; margin: auto; display: flex; flex-direction: row; }\n" +
//                    ".typora-export-sidebar { width: 300px; font-size: 0.8rem; margin-top: 80px; margin-right: 18px; }\n" +
//                    ".typora-export-show-outline #write { --webkit-flex:2; flex: 2 1 0%; }\n" +
//                    ".typora-export-sidebar .outline-content { position: fixed; top: 0px; max-height: 100%; overflow: hidden auto; padding-bottom: 30px; padding-top: 60px; width: 300px; }\n" +
//                    "@media screen and (max-width: 1024px) {\n" +
//                    "  .typora-export-sidebar, .typora-export-sidebar .outline-content { width: 240px; }\n" +
//                    "}\n" +
//                    "@media screen and (max-width: 800px) {\n" +
//                    "  .typora-export-sidebar { display: none; }\n" +
//                    "}\n" +
//                    ".outline-content li, .outline-content ul { margin-left: 0px; margin-right: 0px; padding-left: 0px; padding-right: 0px; list-style: none; }\n" +
//                    ".outline-content ul { margin-top: 0px; margin-bottom: 0px; }\n" +
//                    ".outline-content strong { font-weight: 400; }\n" +
//                    ".outline-expander { width: 1rem; height: 1.42857rem; position: relative; display: table-cell; vertical-align: middle; cursor: pointer; padding-left: 4px; }\n" +
//                    ".outline-expander::before { content: \"\uF125\"; position: relative; font-family: Ionicons; display: inline-block; font-size: 8px; vertical-align: middle; }\n" +
//                    ".outline-item { padding-top: 3px; padding-bottom: 3px; cursor: pointer; }\n" +
//                    ".outline-expander:hover::before { content: \"\uF123\"; }\n" +
//                    ".outline-h1 > .outline-item { padding-left: 0px; }\n" +
//                    ".outline-h2 > .outline-item { padding-left: 1em; }\n" +
//                    ".outline-h3 > .outline-item { padding-left: 2em; }\n" +
//                    ".outline-h4 > .outline-item { padding-left: 3em; }\n" +
//                    ".outline-h5 > .outline-item { padding-left: 4em; }\n" +
//                    ".outline-h6 > .outline-item { padding-left: 5em; }\n" +
//                    ".outline-label { cursor: pointer; display: table-cell; vertical-align: middle; text-decoration: none; color: inherit; }\n" +
//                    ".outline-label:hover { text-decoration: underline; }\n" +
//                    ".outline-item:hover { border-color: rgb(245, 245, 245); background-color: var(--item-hover-bg-color); }\n" +
//                    ".outline-item:hover { margin-left: -28px; margin-right: -28px; border-left: 28px solid transparent; border-right: 28px solid transparent; }\n" +
//                    ".outline-item-single .outline-expander::before, .outline-item-single .outline-expander:hover::before { display: none; }\n" +
//                    ".outline-item-open > .outline-item > .outline-expander::before { content: \"\uF123\"; }\n" +
//                    ".outline-children { display: none; }\n" +
//                    ".info-panel-tab-wrapper { display: none; }\n" +
//                    ".outline-item-open > .outline-children { display: block; }\n" +
//                    ".typora-export .outline-item { padding-top: 1px; padding-bottom: 1px; }\n" +
//                    ".typora-export .outline-item:hover { margin-right: -8px; border-right: 8px solid transparent; }\n" +
//                    ".typora-export .outline-expander::before { content: \"+\"; font-family: inherit; top: -1px; }\n" +
//                    ".typora-export .outline-expander:hover::before, .typora-export .outline-item-open > .outline-item > .outline-expander::before { content: \"−\"; }\n" +
//                    ".typora-export-collapse-outline .outline-children { display: none; }\n" +
//                    ".typora-export-collapse-outline .outline-item-open > .outline-children, .typora-export-no-collapse-outline .outline-children { display: block; }\n" +
//                    ".typora-export-no-collapse-outline .outline-expander::before { content: \"\" !important; }\n" +
//                    ".typora-export-show-outline .outline-item-active > .outline-item .outline-label { font-weight: 700; }\n" +
//                    "\n" +
//                    "\n" +
//                    ".CodeMirror { height: auto; }\n" +
//                    ".CodeMirror.cm-s-inner { background: inherit; }\n" +
//                    ".CodeMirror-scroll { overflow: auto hidden; z-index: 3; }\n" +
//                    ".CodeMirror-gutter-filler, .CodeMirror-scrollbar-filler { background-color: rgb(255, 255, 255); }\n" +
//                    ".CodeMirror-gutters { border-right: 1px solid rgb(221, 221, 221); background: inherit; white-space: nowrap; }\n" +
//                    ".CodeMirror-linenumber { padding: 0px 3px 0px 5px; text-align: right; color: rgb(153, 153, 153); }\n" +
//                    ".cm-s-inner .cm-keyword { color: rgb(119, 0, 136); }\n" +
//                    ".cm-s-inner .cm-atom, .cm-s-inner.cm-atom { color: rgb(34, 17, 153); }\n" +
//                    ".cm-s-inner .cm-number { color: rgb(17, 102, 68); }\n" +
//                    ".cm-s-inner .cm-def { color: rgb(0, 0, 255); }\n" +
//                    ".cm-s-inner .cm-variable { color: rgb(0, 0, 0); }\n" +
//                    ".cm-s-inner .cm-variable-2 { color: rgb(0, 85, 170); }\n" +
//                    ".cm-s-inner .cm-variable-3 { color: rgb(0, 136, 85); }\n" +
//                    ".cm-s-inner .cm-string { color: rgb(170, 17, 17); }\n" +
//                    ".cm-s-inner .cm-property { color: rgb(0, 0, 0); }\n" +
//                    ".cm-s-inner .cm-operator { color: rgb(152, 26, 26); }\n" +
//                    ".cm-s-inner .cm-comment, .cm-s-inner.cm-comment { color: rgb(170, 85, 0); }\n" +
//                    ".cm-s-inner .cm-string-2 { color: rgb(255, 85, 0); }\n" +
//                    ".cm-s-inner .cm-meta { color: rgb(85, 85, 85); }\n" +
//                    ".cm-s-inner .cm-qualifier { color: rgb(85, 85, 85); }\n" +
//                    ".cm-s-inner .cm-builtin { color: rgb(51, 0, 170); }\n" +
//                    ".cm-s-inner .cm-bracket { color: rgb(153, 153, 119); }\n" +
//                    ".cm-s-inner .cm-tag { color: rgb(17, 119, 0); }\n" +
//                    ".cm-s-inner .cm-attribute { color: rgb(0, 0, 204); }\n" +
//                    ".cm-s-inner .cm-header, .cm-s-inner.cm-header { color: rgb(0, 0, 255); }\n" +
//                    ".cm-s-inner .cm-quote, .cm-s-inner.cm-quote { color: rgb(0, 153, 0); }\n" +
//                    ".cm-s-inner .cm-hr, .cm-s-inner.cm-hr { color: rgb(153, 153, 153); }\n" +
//                    ".cm-s-inner .cm-link, .cm-s-inner.cm-link { color: rgb(0, 0, 204); }\n" +
//                    ".cm-negative { color: rgb(221, 68, 68); }\n" +
//                    ".cm-positive { color: rgb(34, 153, 34); }\n" +
//                    ".cm-header, .cm-strong { font-weight: 700; }\n" +
//                    ".cm-del { text-decoration: line-through; }\n" +
//                    ".cm-em { font-style: italic; }\n" +
//                    ".cm-link { text-decoration: underline; }\n" +
//                    ".cm-error { color: red; }\n" +
//                    ".cm-invalidchar { color: red; }\n" +
//                    ".cm-constant { color: rgb(38, 139, 210); }\n" +
//                    ".cm-defined { color: rgb(181, 137, 0); }\n" +
//                    "div.CodeMirror span.CodeMirror-matchingbracket { color: rgb(0, 255, 0); }\n" +
//                    "div.CodeMirror span.CodeMirror-nonmatchingbracket { color: rgb(255, 34, 34); }\n" +
//                    ".cm-s-inner .CodeMirror-activeline-background { background: inherit; }\n" +
//                    ".CodeMirror { position: relative; overflow: hidden; }\n" +
//                    ".CodeMirror-scroll { height: 100%; outline: 0px; position: relative; box-sizing: content-box; background: inherit; }\n" +
//                    ".CodeMirror-sizer { position: relative; }\n" +
//                    ".CodeMirror-gutter-filler, .CodeMirror-hscrollbar, .CodeMirror-scrollbar-filler, .CodeMirror-vscrollbar { position: absolute; z-index: 6; display: none; }\n" +
//                    ".CodeMirror-vscrollbar { right: 0px; top: 0px; overflow: hidden; }\n" +
//                    ".CodeMirror-hscrollbar { bottom: 0px; left: 0px; overflow: hidden; }\n" +
//                    ".CodeMirror-scrollbar-filler { right: 0px; bottom: 0px; }\n" +
//                    ".CodeMirror-gutter-filler { left: 0px; bottom: 0px; }\n" +
//                    ".CodeMirror-gutters { position: absolute; left: 0px; top: 0px; padding-bottom: 30px; z-index: 3; }\n" +
//                    ".CodeMirror-gutter { white-space: normal; height: 100%; box-sizing: content-box; padding-bottom: 30px; margin-bottom: -32px; display: inline-block; }\n" +
//                    ".CodeMirror-gutter-wrapper { position: absolute; z-index: 4; background: 0px 0px !important; border: none !important; }\n" +
//                    ".CodeMirror-gutter-background { position: absolute; top: 0px; bottom: 0px; z-index: 4; }\n" +
//                    ".CodeMirror-gutter-elt { position: absolute; cursor: default; z-index: 4; }\n" +
//                    ".CodeMirror-lines { cursor: text; }\n" +
//                    ".CodeMirror pre { border-radius: 0px; border-width: 0px; background: 0px 0px; font-family: inherit; font-size: inherit; margin: 0px; white-space: pre; overflow-wrap: normal; color: inherit; z-index: 2; position: relative; overflow: visible; }\n" +
//                    ".CodeMirror-wrap pre { overflow-wrap: break-word; white-space: pre-wrap; word-break: normal; }\n" +
//                    ".CodeMirror-code pre { border-right: 30px solid transparent; width: fit-content; }\n" +
//                    ".CodeMirror-wrap .CodeMirror-code pre { border-right: none; width: auto; }\n" +
//                    ".CodeMirror-linebackground { position: absolute; left: 0px; right: 0px; top: 0px; bottom: 0px; z-index: 0; }\n" +
//                    ".CodeMirror-linewidget { position: relative; z-index: 2; overflow: auto; }\n" +
//                    ".CodeMirror-wrap .CodeMirror-scroll { overflow-x: hidden; }\n" +
//                    ".CodeMirror-measure { position: absolute; width: 100%; height: 0px; overflow: hidden; visibility: hidden; }\n" +
//                    ".CodeMirror-measure pre { position: static; }\n" +
//                    ".CodeMirror div.CodeMirror-cursor { position: absolute; visibility: hidden; border-right: none; width: 0px; }\n" +
//                    ".CodeMirror div.CodeMirror-cursor { visibility: hidden; }\n" +
//                    ".CodeMirror-focused div.CodeMirror-cursor { visibility: inherit; }\n" +
//                    ".cm-searching { background: rgba(255, 255, 0, 0.4); }\n" +
//                    "span.cm-underlined { text-decoration: underline; }\n" +
//                    "span.cm-strikethrough { text-decoration: line-through; }\n" +
//                    ".cm-tw-syntaxerror { color: rgb(255, 255, 255); background-color: rgb(153, 0, 0); }\n" +
//                    ".cm-tw-deleted { text-decoration: line-through; }\n" +
//                    ".cm-tw-header5 { font-weight: 700; }\n" +
//                    ".cm-tw-listitem:first-child { padding-left: 10px; }\n" +
//                    ".cm-tw-box { border-style: solid; border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px; border-color: inherit; border-top-width: 0px !important; }\n" +
//                    ".cm-tw-underline { text-decoration: underline; }\n" +
//                    "@media print {\n" +
//                    "  .CodeMirror div.CodeMirror-cursor { visibility: hidden; }\n" +
//                    "}\n" +
//                    "\n" +
//                    "\n" +
//                    ":root {\n" +
//                    "    --side-bar-bg-color: #fafafa;\n" +
//                    "    --control-text-color: #777;\n" +
//                    "}\n" +
//                    "\n" +
//                    "@include-when-export url(https://fonts.loli.net/css?family=Open+Sans:400italic,700italic,700,400&subset=latin,latin-ext);\n" +
//                    "\n" +
//                    "/* open-sans-regular - latin-ext_latin */\n" +
//                    "  /* open-sans-italic - latin-ext_latin */\n" +
//                    "    /* open-sans-700 - latin-ext_latin */\n" +
//                    "    /* open-sans-700italic - latin-ext_latin */\n" +
//                    "  html {\n" +
//                    "    font-size: 16px;\n" +
//                    "}\n" +
//                    "\n" +
//                    "body {\n" +
//                    "    font-family: \"Open Sans\",\"Clear Sans\", \"Helvetica Neue\", Helvetica, Arial, sans-serif;\n" +
//                    "    color: rgb(51, 51, 51);\n" +
//                    "    line-height: 1.6;\n" +
//                    "}\n" +
//                    "\n" +
//                    "#write {\n" +
//                    "    max-width: 860px;\n" +
//                    "  \tmargin: 0 auto;\n" +
//                    "  \tpadding: 30px;\n" +
//                    "    padding-bottom: 100px;\n" +
//                    "}\n" +
//                    "\n" +
//                    "@media only screen and (min-width: 1400px) {\n" +
//                    "\t#write {\n" +
//                    "\t\tmax-width: 1024px;\n" +
//                    "\t}\n" +
//                    "}\n" +
//                    "\n" +
//                    "@media only screen and (min-width: 1800px) {\n" +
//                    "\t#write {\n" +
//                    "\t\tmax-width: 1200px;\n" +
//                    "\t}\n" +
//                    "}\n" +
//                    "\n" +
//                    "#write > ul:first-child,\n" +
//                    "#write > ol:first-child{\n" +
//                    "    margin-top: 30px;\n" +
//                    "}\n" +
//                    "\n" +
//                    "a {\n" +
//                    "    color: #4183C4;\n" +
//                    "}\n" +
//                    "h1,\n" +
//                    "h2,\n" +
//                    "h3,\n" +
//                    "h4,\n" +
//                    "h5,\n" +
//                    "h6 {\n" +
//                    "    position: relative;\n" +
//                    "    margin-top: 1rem;\n" +
//                    "    margin-bottom: 1rem;\n" +
//                    "    font-weight: bold;\n" +
//                    "    line-height: 1.4;\n" +
//                    "    cursor: text;\n" +
//                    "}\n" +
//                    "h1:hover a.anchor,\n" +
//                    "h2:hover a.anchor,\n" +
//                    "h3:hover a.anchor,\n" +
//                    "h4:hover a.anchor,\n" +
//                    "h5:hover a.anchor,\n" +
//                    "h6:hover a.anchor {\n" +
//                    "    text-decoration: none;\n" +
//                    "}\n" +
//                    "h1 tt,\n" +
//                    "h1 code {\n" +
//                    "    font-size: inherit;\n" +
//                    "}\n" +
//                    "h2 tt,\n" +
//                    "h2 code {\n" +
//                    "    font-size: inherit;\n" +
//                    "}\n" +
//                    "h3 tt,\n" +
//                    "h3 code {\n" +
//                    "    font-size: inherit;\n" +
//                    "}\n" +
//                    "h4 tt,\n" +
//                    "h4 code {\n" +
//                    "    font-size: inherit;\n" +
//                    "}\n" +
//                    "h5 tt,\n" +
//                    "h5 code {\n" +
//                    "    font-size: inherit;\n" +
//                    "}\n" +
//                    "h6 tt,\n" +
//                    "h6 code {\n" +
//                    "    font-size: inherit;\n" +
//                    "}\n" +
//                    "h1 {\n" +
//                    "    font-size: 2.25em;\n" +
//                    "    line-height: 1.2;\n" +
//                    "    border-bottom: 1px solid #eee;\n" +
//                    "}\n" +
//                    "h2 {\n" +
//                    "    font-size: 1.75em;\n" +
//                    "    line-height: 1.225;\n" +
//                    "    border-bottom: 1px solid #eee;\n" +
//                    "}\n" +
//                    "\n" +
//                    "/*@media print {\n" +
//                    "    .typora-export h1,\n" +
//                    "    .typora-export h2 {\n" +
//                    "        border-bottom: none;\n" +
//                    "        padding-bottom: initial;\n" +
//                    "    }\n" +
//                    "\n" +
//                    "    .typora-export h1::after,\n" +
//                    "    .typora-export h2::after {\n" +
//                    "        content: \"\";\n" +
//                    "        display: block;\n" +
//                    "        height: 100px;\n" +
//                    "        margin-top: -96px;\n" +
//                    "        border-top: 1px solid #eee;\n" +
//                    "    }\n" +
//                    "}*/\n" +
//                    "\n" +
//                    "h3 {\n" +
//                    "    font-size: 1.5em;\n" +
//                    "    line-height: 1.43;\n" +
//                    "}\n" +
//                    "h4 {\n" +
//                    "    font-size: 1.25em;\n" +
//                    "}\n" +
//                    "h5 {\n" +
//                    "    font-size: 1em;\n" +
//                    "}\n" +
//                    "h6 {\n" +
//                    "   font-size: 1em;\n" +
//                    "    color: #777;\n" +
//                    "}\n" +
//                    "p,\n" +
//                    "blockquote,\n" +
//                    "ul,\n" +
//                    "ol,\n" +
//                    "dl,\n" +
//                    "table{\n" +
//                    "    margin: 0.8em 0;\n" +
//                    "}\n" +
//                    "li>ol,\n" +
//                    "li>ul {\n" +
//                    "    margin: 0 0;\n" +
//                    "}\n" +
//                    "hr {\n" +
//                    "    height: 2px;\n" +
//                    "    padding: 0;\n" +
//                    "    margin: 16px 0;\n" +
//                    "    background-color: #e7e7e7;\n" +
//                    "    border: 0 none;\n" +
//                    "    overflow: hidden;\n" +
//                    "    box-sizing: content-box;\n" +
//                    "}\n" +
//                    "\n" +
//                    "li p.first {\n" +
//                    "    display: inline-block;\n" +
//                    "}\n" +
//                    "ul,\n" +
//                    "ol {\n" +
//                    "    padding-left: 30px;\n" +
//                    "}\n" +
//                    "ul:first-child,\n" +
//                    "ol:first-child {\n" +
//                    "    margin-top: 0;\n" +
//                    "}\n" +
//                    "ul:last-child,\n" +
//                    "ol:last-child {\n" +
//                    "    margin-bottom: 0;\n" +
//                    "}\n" +
//                    "blockquote {\n" +
//                    "    border-left: 4px solid #dfe2e5;\n" +
//                    "    padding: 0 15px;\n" +
//                    "    color: #777777;\n" +
//                    "}\n" +
//                    "blockquote blockquote {\n" +
//                    "    padding-right: 0;\n" +
//                    "}\n" +
//                    "table {\n" +
//                    "    padding: 0;\n" +
//                    "    word-break: initial;\n" +
//                    "}\n" +
//                    "table tr {\n" +
//                    "    border: 1px solid #dfe2e5;\n" +
//                    "    margin: 0;\n" +
//                    "    padding: 0;\n" +
//                    "}\n" +
//                    "table tr:nth-child(2n),\n" +
//                    "thead {\n" +
//                    "    background-color: #f8f8f8;\n" +
//                    "}\n" +
//                    "table th {\n" +
//                    "    font-weight: bold;\n" +
//                    "    border: 1px solid #dfe2e5;\n" +
//                    "    border-bottom: 0;\n" +
//                    "    margin: 0;\n" +
//                    "    padding: 6px 13px;\n" +
//                    "}\n" +
//                    "table td {\n" +
//                    "    border: 1px solid #dfe2e5;\n" +
//                    "    margin: 0;\n" +
//                    "    padding: 6px 13px;\n" +
//                    "}\n" +
//                    "table th:first-child,\n" +
//                    "table td:first-child {\n" +
//                    "    margin-top: 0;\n" +
//                    "}\n" +
//                    "table th:last-child,\n" +
//                    "table td:last-child {\n" +
//                    "    margin-bottom: 0;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".CodeMirror-lines {\n" +
//                    "    padding-left: 4px;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".code-tooltip {\n" +
//                    "    box-shadow: 0 1px 1px 0 rgba(0,28,36,.3);\n" +
//                    "    border-top: 1px solid #eef2f2;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".md-fences,\n" +
//                    "code,\n" +
//                    "tt {\n" +
//                    "    border: 1px solid #e7eaed;\n" +
//                    "    background-color: #f8f8f8;\n" +
//                    "    border-radius: 3px;\n" +
//                    "    padding: 0;\n" +
//                    "    padding: 2px 4px 0px 4px;\n" +
//                    "    font-size: 0.9em;\n" +
//                    "}\n" +
//                    "\n" +
//                    "code {\n" +
//                    "    background-color: #f3f4f4;\n" +
//                    "    padding: 0 2px 0 2px;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".md-fences {\n" +
//                    "    margin-bottom: 15px;\n" +
//                    "    margin-top: 15px;\n" +
//                    "    padding-top: 8px;\n" +
//                    "    padding-bottom: 6px;\n" +
//                    "}\n" +
//                    "\n" +
//                    "\n" +
//                    ".md-task-list-item > input {\n" +
//                    "  margin-left: -1.3em;\n" +
//                    "}\n" +
//                    "\n" +
//                    "@media print {\n" +
//                    "    html {\n" +
//                    "        font-size: 13px;\n" +
//                    "    }\n" +
//                    "    table,\n" +
//                    "    pre {\n" +
//                    "        page-break-inside: avoid;\n" +
//                    "    }\n" +
//                    "    pre {\n" +
//                    "        word-wrap: break-word;\n" +
//                    "    }\n" +
//                    "}\n" +
//                    "\n" +
//                    ".md-fences {\n" +
//                    "\tbackground-color: #f8f8f8;\n" +
//                    "}\n" +
//                    "#write pre.md-meta-block {\n" +
//                    "\tpadding: 1rem;\n" +
//                    "    font-size: 85%;\n" +
//                    "    line-height: 1.45;\n" +
//                    "    background-color: #f7f7f7;\n" +
//                    "    border: 0;\n" +
//                    "    border-radius: 3px;\n" +
//                    "    color: #777777;\n" +
//                    "    margin-top: 0 !important;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".mathjax-block>.code-tooltip {\n" +
//                    "\tbottom: .375rem;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".md-mathjax-midline {\n" +
//                    "    background: #fafafa;\n" +
//                    "}\n" +
//                    "\n" +
//                    "#write>h3.md-focus:before{\n" +
//                    "\tleft: -1.5625rem;\n" +
//                    "\ttop: .375rem;\n" +
//                    "}\n" +
//                    "#write>h4.md-focus:before{\n" +
//                    "\tleft: -1.5625rem;\n" +
//                    "\ttop: .285714286rem;\n" +
//                    "}\n" +
//                    "#write>h5.md-focus:before{\n" +
//                    "\tleft: -1.5625rem;\n" +
//                    "\ttop: .285714286rem;\n" +
//                    "}\n" +
//                    "#write>h6.md-focus:before{\n" +
//                    "\tleft: -1.5625rem;\n" +
//                    "\ttop: .285714286rem;\n" +
//                    "}\n" +
//                    ".md-image>.md-meta {\n" +
//                    "    /*border: 1px solid #ddd;*/\n" +
//                    "    border-radius: 3px;\n" +
//                    "    padding: 2px 0px 0px 4px;\n" +
//                    "    font-size: 0.9em;\n" +
//                    "    color: inherit;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".md-tag {\n" +
//                    "    color: #a7a7a7;\n" +
//                    "    opacity: 1;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".md-toc { \n" +
//                    "    margin-top:20px;\n" +
//                    "    padding-bottom:20px;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".sidebar-tabs {\n" +
//                    "    border-bottom: none;\n" +
//                    "}\n" +
//                    "\n" +
//                    "#typora-quick-open {\n" +
//                    "    border: 1px solid #ddd;\n" +
//                    "    background-color: #f8f8f8;\n" +
//                    "}\n" +
//                    "\n" +
//                    "#typora-quick-open-item {\n" +
//                    "    background-color: #FAFAFA;\n" +
//                    "    border-color: #FEFEFE #e5e5e5 #e5e5e5 #eee;\n" +
//                    "    border-style: solid;\n" +
//                    "    border-width: 1px;\n" +
//                    "}\n" +
//                    "\n" +
//                    "/** focus mode */\n" +
//                    ".on-focus-mode blockquote {\n" +
//                    "    border-left-color: rgba(85, 85, 85, 0.12);\n" +
//                    "}\n" +
//                    "\n" +
//                    "header, .context-menu, .megamenu-content, footer{\n" +
//                    "    font-family: \"Segoe UI\", \"Arial\", sans-serif;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".file-node-content:hover .file-node-icon,\n" +
//                    ".file-node-content:hover .file-node-open-state{\n" +
//                    "    visibility: visible;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".mac-seamless-mode #typora-sidebar {\n" +
//                    "    background-color: #fafafa;\n" +
//                    "    background-color: var(--side-bar-bg-color);\n" +
//                    "}\n" +
//                    "\n" +
//                    ".md-lang {\n" +
//                    "    color: #b4654d;\n" +
//                    "}\n" +
//                    "\n" +
//                    "/*.html-for-mac {\n" +
//                    "    --item-hover-bg-color: #E6F0FE;\n" +
//                    "}*/\n" +
//                    "\n" +
//                    "#md-notification .btn {\n" +
//                    "    border: 0;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".dropdown-menu .divider {\n" +
//                    "    border-color: #e5e5e5;\n" +
//                    "    opacity: 0.4;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".ty-preferences .window-content {\n" +
//                    "    background-color: #fafafa;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".ty-preferences .nav-group-item.active {\n" +
//                    "    color: white;\n" +
//                    "    background: #999;\n" +
//                    "}\n" +
//                    "\n" +
//                    ".menu-item-container a.menu-style-btn {\n" +
//                    "    background-color: #f5f8fa;\n" +
//                    "    background-image: linear-gradient( 180deg , hsla(0, 0%, 100%, 0.8), hsla(0, 0%, 100%, 0)); \n" +
//                    "}\n" +
//                    "\n" +
//                    "\n" +
//                    " :root {--mermaid-font-zoom:1.25em ;} ";
//            MD_CSS = "<style type=\"text/css\">\n" + MD_CSS + "\n</style>\n";
//        } catch (Exception e) {
//            MD_CSS = "";
//        }
//    }
////
////
////    /**
////     * 将本地的markdown文件，转为html文档输出
////     *
////     * @param path 相对地址or绝对地址 ("/" 开头)
////     * @return
////     * @throws IOException
////     */
////    public static MarkdownEntity ofFile(String path) throws IOException {
////        return ofStream(FileReadUtil.getStreamByFileName(path));
////    }
////
////
////    /**
////     * 将网络的markdown文件，转为html文档输出
////     *
////     * @param url http开头的url格式
////     * @return
////     * @throws IOException
////     */
////    public static MarkdownEntity ofUrl(String url) throws IOException {
////        return ofStream(FileReadUtil.getStreamByFileName(url));
////    }
////
////
////    /**
////     * 将流转为html文档输出
////     *
////     * @param stream
////     * @return
////     */
////    public static MarkdownEntity ofStream(InputStream stream) {
////        BufferedReader bufferedReader = new BufferedReader(
////                new InputStreamReader(stream, Charset.forName("UTF-8")));
////        List<String> lines = bufferedReader.lines().collect(Collectors.toList());
////        String content = Joiner.on("\n").join(lines);
////        return ofContent(content);
////    }
//
//
//    /**
//     * 直接将markdown语义的文本转为html格式输出
//     *
//     * @param content markdown语义文本
//     * @return
//     */
//    public static MarkdownEntity ofContent(String content) {
//        String html = parse(content);
//        MarkdownEntity entity = new MarkdownEntity();
//        entity.setCss(MD_CSS);
//        entity.setHtml(html);
//        entity.addDivStyle("class", "CodeMirror cm-s-inner CodeMirror-wrap ");
//        return entity;
//    }
//
//
//    /**
//     * markdown to image
//     *
//     * @param content markdown contents
//     * @return parse html contents
//     */
//    public static String parse(String content) {
//        MutableDataSet options = new MutableDataSet();
//        options.setFrom(ParserEmulationProfile.MARKDOWN);
//
//        // enable table parse!
//        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create()));
//
//
//        Parser parser = Parser.builder(options).build();
//        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
//
//        Node document = parser.parse(content);
//        return renderer.render(document);
//    }
//}