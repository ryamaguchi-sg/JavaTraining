from pathlib import Path
from jinja2 import Environment, FileSystemLoader, select_autoescape
import logging as log


log.basicConfig(level=log.INFO, format="%(levelname)s: %(message)s")


def main():
    source_path = Path(".")
    encoding = "utf-8"

    env = Environment(
        loader=FileSystemLoader(str(source_path), encoding=encoding),
        autoescape=select_autoescape()
    )

    for src_file_path in source_path.glob("**/*.html.j2"):
        dest_file_path = Path(src_file_path.stem)
        if (dest_file_path.exists() and 
            src_file_path.stat().st_mtime > dest_file_path.stat().st_mtime):
            template = env.get_template(str(src_file_path))
            dest_file_path.write_text(template.render(), encoding=encoding)
            log.info("[Generated] {} -> {}".format(str(src_file_path), str(dest_file_path)))
        else:
            log.info("[Skipped  ] {} -> {}".format(str(src_file_path), str(dest_file_path)))


if __name__ == '__main__':
    main()