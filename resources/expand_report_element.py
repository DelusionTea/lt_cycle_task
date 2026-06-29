class ExpandReportElement:
    def __init__(self, title: str, content: str):
        self.title = title
        self.content = content

    def to_expand_macro(self):
        """Создание макроса ui-expand"""
        return f"""
        <ac:structured-macro ac:name="ui-expand" ac:schema-version="1">
            <ac:parameter ac:name="title">{self.title}</ac:parameter>
            <ac:parameter ac:name="expanded">false</ac:parameter>
            <ac:rich-text-body>
                {self.content}
            </ac:rich-text-body>
        </ac:structured-macro>"""
