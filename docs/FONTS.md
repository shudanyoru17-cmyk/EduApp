# Fonts

The app ships the **Cairo** typeface (Google Fonts, SIL Open Font License 1.1).

## Files (`app/src/main/res/font/`)
| File | Weight |
|------|--------|
| `cairo_regular.ttf` | 400 |
| `cairo_medium.ttf`  | 500 |
| `cairo_bold.ttf`    | 700 |
| `cairo.xml`         | font-family mapping the three weights |

These static instances were generated from the official Cairo **variable** font
(`fonttools varLib.instancer ... wght=400/500/700`).

## Usage
- **Compose:** `core/ui/Theme.kt` builds a `FontFamily(Font(R.font.cairo_*))` and
  applies it to every Material 3 text style in `AppTypography`.
- **View/XML:** `android:fontFamily="@font/cairo"` (set on `Theme.EduApp`).

## License
Cairo is licensed under the SIL Open Font License, Version 1.1 — full text in
`THIRD_PARTY_LICENSES/Cairo-OFL.txt`. The OFL permits bundling and redistribution
within applications.
