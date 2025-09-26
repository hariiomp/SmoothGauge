# SmoothGauge - Jetpack Compose

**SmoothGauge** is a highly interactive, animated gauge component built using **Jetpack Compose**. It allows users to select values by dragging along a semi-circular gauge, with smooth animations and dynamic color transitions.

---

## Features

- Fully **customizable** gauge with configurable `minValue`, `maxValue`, and `initialValue`.
- **Smooth animated transitions** when the value changes.
- **Drag-to-set** functionality for interactive value adjustment.
- **Dynamic color gradient** on ticks based on value.
- Fully built in **Jetpack Compose**, no XML required.
- Preview-ready composable for quick testing.

---

## Preview
https://github.com/user-attachments/assets/ece10823-5d80-499c-9047-057cfb622560


## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/hariiomp/SmoothGauge.git

## What I Used to Build This
- Language: Kotlin
- UI Framework: Jetpack Compose
- Animations: Animatable, spring physics-based animation
- Gestures: pointerInput with detectDragGestures
- Canvas Drawing: Custom tick marks & needle rendering
- Color Interpolation: lerpTo for gradient-style effects
- Previewing: @Preview annotations for design-time UI testing

## 📷 Preview  

```kotlin
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSmoothGauge() {
    SmoothGauge(
        modifier = Modifier.fillMaxWidth(),
        initialValue = 50
    )
}
