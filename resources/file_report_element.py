class FileReportElement:
    def __init__(self, name: str, height: int):
        self.name = name
        self.height = height

    def to_macro(self):
        """Создание макроса для отображения файла"""
        return f"""<p>
            <ac:structured-macro ac:name="view-file" ac:schema-version="1">
                <ac:parameter ac:name="name">
                    <ri:attachment ri:filename="{self.name}" />
                </ac:parameter>
                <ac:parameter ac:name="height">{self.height}</ac:parameter>
            </ac:structured-macro>
        </p>
        """
