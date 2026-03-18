# Frost UI Design Specification

Design token reference and component specifications for the Tranzo Wallet Frost UI system. This document serves as a source of truth for recreating the design in Figma or any design tool.

---

## Color Tokens

```json
{
  "colors": {
    "frost-background": {
      "hex": "#0A0E1A",
      "rgb": "10, 14, 26",
      "usage": "App background, scaffold background"
    },
    "frost-surface": {
      "hex": "#111827",
      "rgb": "17, 24, 39",
      "usage": "Elevated surfaces, bottom sheet background"
    },
    "frost-card": {
      "hex": "#1F2937",
      "rgb": "31, 41, 55",
      "opacity": 0.8,
      "usage": "Card backgrounds (with glassmorphic blur)"
    },
    "icy-blue": {
      "hex": "#60A5FA",
      "rgb": "96, 165, 250",
      "usage": "Primary accent, links, primary button gradient start"
    },
    "frost-cyan": {
      "hex": "#22D3EE",
      "rgb": "34, 211, 238",
      "usage": "Secondary accent, primary button gradient end, highlights"
    },
    "frost-white": {
      "hex": "#F9FAFB",
      "rgb": "249, 250, 251",
      "usage": "Primary text, button labels, headings"
    },
    "frost-gray": {
      "hex": "#9CA3AF",
      "rgb": "156, 163, 175",
      "usage": "Secondary text, captions, placeholders, disabled text"
    },
    "frost-slate": {
      "hex": "#334155",
      "rgb": "51, 65, 85",
      "usage": "Dividers, disabled button backgrounds, input borders"
    },
    "frost-dark-slate": {
      "hex": "#1E293B",
      "rgb": "30, 41, 59",
      "usage": "Bottom navigation background, card variants"
    },
    "frost-success": {
      "hex": "#34D399",
      "rgb": "52, 211, 153",
      "usage": "Success states, positive balance changes, received transactions"
    },
    "frost-error": {
      "hex": "#F87171",
      "rgb": "248, 113, 113",
      "usage": "Error states, negative balance changes, failed transactions"
    },
    "frost-warning": {
      "hex": "#FBBF24",
      "rgb": "251, 191, 36",
      "usage": "Warning states, pending transactions"
    },
    "frost-purple": {
      "hex": "#818CF8",
      "rgb": "129, 140, 248",
      "usage": "NFT accents, secondary highlights"
    },
    "frost-translucent": {
      "hex": "#FFFFFF",
      "opacity": 0.12,
      "usage": "Glassmorphic card fill, translucent overlays"
    },
    "frost-border": {
      "hex": "#FFFFFF",
      "opacity": 0.2,
      "usage": "Card borders, subtle dividers"
    }
  },
  "gradients": {
    "primary-button": {
      "type": "linear",
      "direction": "horizontal",
      "stops": [
        { "color": "#60A5FA", "position": 0 },
        { "color": "#22D3EE", "position": 1 }
      ],
      "usage": "Primary button background"
    },
    "balance-card": {
      "type": "linear",
      "direction": "135deg",
      "stops": [
        { "color": "#60A5FA", "position": 0, "opacity": 0.15 },
        { "color": "#22D3EE", "position": 1, "opacity": 0.05 }
      ],
      "usage": "Balance card background overlay"
    }
  }
}
```

---

## Typography Scale

```json
{
  "typography": {
    "font-family": "System Default (Inter / Roboto)",
    "styles": {
      "display-large": {
        "size": 36,
        "unit": "sp",
        "weight": 700,
        "weight-name": "Bold",
        "letter-spacing": -0.5,
        "color": "frost-white",
        "usage": "Portfolio total balance"
      },
      "headline-large": {
        "size": 28,
        "unit": "sp",
        "weight": 600,
        "weight-name": "SemiBold",
        "letter-spacing": 0,
        "color": "frost-white",
        "usage": "Screen titles (large)"
      },
      "headline-medium": {
        "size": 24,
        "unit": "sp",
        "weight": 600,
        "weight-name": "SemiBold",
        "letter-spacing": 0,
        "color": "frost-white",
        "usage": "Section headings, card titles"
      },
      "title-large": {
        "size": 20,
        "unit": "sp",
        "weight": 600,
        "weight-name": "SemiBold",
        "letter-spacing": 0,
        "color": "frost-white",
        "usage": "App bar titles, sub-section headings"
      },
      "title-medium": {
        "size": 18,
        "unit": "sp",
        "weight": 500,
        "weight-name": "Medium",
        "letter-spacing": 0,
        "color": "frost-white",
        "usage": "Token names, list item titles"
      },
      "body-large": {
        "size": 16,
        "unit": "sp",
        "weight": 400,
        "weight-name": "Normal",
        "line-height": 24,
        "letter-spacing": 0,
        "color": "frost-white",
        "usage": "Body text, descriptions, input text"
      },
      "body-medium": {
        "size": 14,
        "unit": "sp",
        "weight": 400,
        "weight-name": "Normal",
        "line-height": 20,
        "letter-spacing": 0,
        "color": "frost-white",
        "usage": "Secondary body text, list item subtitles"
      },
      "label-large": {
        "size": 14,
        "unit": "sp",
        "weight": 500,
        "weight-name": "Medium",
        "letter-spacing": 0.1,
        "color": "frost-white",
        "usage": "Button labels, tab labels, chip text"
      },
      "label-medium": {
        "size": 12,
        "unit": "sp",
        "weight": 500,
        "weight-name": "Medium",
        "letter-spacing": 0,
        "color": "frost-white",
        "usage": "Badges, small labels"
      },
      "caption": {
        "size": 12,
        "unit": "sp",
        "weight": 400,
        "weight-name": "Normal",
        "letter-spacing": 0,
        "color": "frost-gray",
        "usage": "Timestamps, captions, helper text"
      },
      "label-small": {
        "size": 11,
        "unit": "sp",
        "weight": 400,
        "weight-name": "Normal",
        "letter-spacing": 0,
        "color": "frost-gray",
        "usage": "Footnotes, fine print"
      }
    }
  }
}
```

---

## Spacing System

```json
{
  "spacing": {
    "xs":  { "value": 4,  "unit": "dp", "usage": "Inline element gaps, icon-to-text spacing" },
    "sm":  { "value": 8,  "unit": "dp", "usage": "Compact list item padding, chip margins" },
    "md":  { "value": 16, "unit": "dp", "usage": "Standard padding, card content insets, screen horizontal margins" },
    "lg":  { "value": 24, "unit": "dp", "usage": "Section spacing, card-to-card gaps" },
    "xl":  { "value": 32, "unit": "dp", "usage": "Major section dividers, top/bottom screen padding" },
    "xxl": { "value": 48, "unit": "dp", "usage": "Hero section spacing, splash screen padding" }
  }
}
```

---

## Elevation & Shadows

```json
{
  "elevation": {
    "none":     { "value": 0,  "unit": "dp", "usage": "Flat elements, backgrounds" },
    "low":      { "value": 2,  "unit": "dp", "usage": "Cards, list items" },
    "medium":   { "value": 4,  "unit": "dp", "usage": "Pressed buttons, raised cards" },
    "high":     { "value": 8,  "unit": "dp", "usage": "Modal sheets, dialogs" },
    "floating": { "value": 16, "unit": "dp", "usage": "FAB, floating send button" }
  }
}
```

---

## Corner Radius

```json
{
  "radius": {
    "sm":   { "value": 8,   "unit": "dp", "usage": "Chips, small badges, input fields" },
    "md":   { "value": 16,  "unit": "dp", "usage": "Buttons, medium cards" },
    "lg":   { "value": 20,  "unit": "dp", "usage": "Primary cards (FrostCard)" },
    "xl":   { "value": 24,  "unit": "dp", "usage": "Modal sheets, large cards" },
    "full": { "value": 100, "unit": "dp", "usage": "Circular buttons, avatars, pills" }
  }
}
```

---

## Component Specifications

### FrostCard

```json
{
  "component": "FrostCard",
  "type": "container",
  "properties": {
    "background": "frost-translucent (#FFFFFF @ 12% opacity)",
    "border": { "width": 1, "color": "frost-border (#FFFFFF @ 20% opacity)" },
    "corner-radius": 20,
    "padding": 16,
    "elevation": 2,
    "blur": 0.5,
    "fill-width": true
  },
  "variants": {
    "default": { "clickable": false },
    "clickable": { "clickable": true, "ripple-color": "frost-translucent" }
  },
  "figma-notes": "Use a rectangle with background blur (layer blur 8-12px) and the translucent fill. Add an inner shadow (white, 5% opacity, 1px) for the frost edge effect."
}
```

### FrostButtonPrimary

```json
{
  "component": "FrostButtonPrimary",
  "type": "button",
  "properties": {
    "width": "fill-parent",
    "height": 56,
    "corner-radius": 16,
    "background": "gradient: icy-blue -> frost-cyan (horizontal)",
    "text-style": "label-large",
    "text-color": "frost-white",
    "elevation-default": 2,
    "elevation-pressed": 4
  },
  "states": {
    "default": { "background": "gradient", "text-color": "frost-white" },
    "disabled": { "background": "frost-slate @ 50%", "text-color": "frost-slate" },
    "loading": { "content": "CircularProgressIndicator (24dp, white, 2dp stroke)" }
  },
  "figma-notes": "Apply horizontal gradient fill from #60A5FA to #22D3EE. Disabled state uses solid frost-slate at 50% opacity."
}
```

### FrostButtonSecondary

```json
{
  "component": "FrostButtonSecondary",
  "type": "button",
  "properties": {
    "width": "fill-parent",
    "height": 56,
    "corner-radius": 16,
    "background": "transparent",
    "border": { "width": 1, "color": "icy-blue" },
    "text-style": "label-large",
    "text-color": "icy-blue"
  },
  "states": {
    "default": { "border-color": "icy-blue", "text-color": "icy-blue" },
    "disabled": { "border-color": "frost-slate", "text-color": "frost-slate" }
  }
}
```

### FrostAppBar

```json
{
  "component": "FrostAppBar",
  "type": "navigation",
  "properties": {
    "height": 56,
    "background": "transparent or frost-surface",
    "title-style": "title-large",
    "title-color": "frost-white",
    "icon-color": "frost-white",
    "icon-size": 24,
    "horizontal-padding": 16
  }
}
```

### FrostModalSheet

```json
{
  "component": "FrostModalSheet",
  "type": "overlay",
  "properties": {
    "background": "frost-surface",
    "corner-radius-top": 24,
    "handle-bar": { "width": 40, "height": 4, "color": "frost-slate", "radius": "full", "top-margin": 12 },
    "content-padding": 16,
    "scrim-color": "#000000 @ 50%",
    "elevation": 8
  }
}
```

### WalletBalanceCard

```json
{
  "component": "WalletBalanceCard",
  "type": "display",
  "properties": {
    "background": "FrostCard base + balance-card gradient overlay",
    "corner-radius": 20,
    "padding": 24,
    "layout": "vertical",
    "children": [
      { "element": "network-label", "style": "caption", "color": "frost-gray" },
      { "element": "total-balance", "style": "display-large", "color": "frost-white", "top-margin": 4 },
      { "element": "balance-change", "style": "body-medium", "color": "frost-success or frost-error", "top-margin": 4 },
      { "element": "address-row", "style": "caption", "color": "frost-gray", "top-margin": 16, "truncated": true, "copy-icon": true }
    ]
  }
}
```

### TokenListItem

```json
{
  "component": "TokenListItem",
  "type": "list-item",
  "properties": {
    "height": 72,
    "horizontal-padding": 16,
    "vertical-padding": 12,
    "layout": "horizontal",
    "children": [
      { "element": "token-icon", "size": 40, "corner-radius": "full", "left": 0 },
      { "element": "name-column", "layout": "vertical", "left-margin": 12, "children": [
        { "element": "token-name", "style": "title-medium", "color": "frost-white" },
        { "element": "token-symbol", "style": "caption", "color": "frost-gray" }
      ]},
      { "element": "balance-column", "layout": "vertical", "align": "end", "children": [
        { "element": "token-balance", "style": "body-large", "color": "frost-white" },
        { "element": "usd-value", "style": "caption", "color": "frost-gray" }
      ]}
    ]
  }
}
```

### FloatingSendButton

```json
{
  "component": "FloatingSendButton",
  "type": "fab",
  "properties": {
    "size": 56,
    "corner-radius": "full",
    "background": "gradient: icy-blue -> frost-cyan",
    "icon": "arrow-upward (send)",
    "icon-size": 24,
    "icon-color": "frost-white",
    "elevation": 16,
    "position": "bottom-end",
    "margin-bottom": 16,
    "margin-end": 16
  }
}
```

### SecureInputField

```json
{
  "component": "SecureInputField",
  "type": "input",
  "properties": {
    "height": 56,
    "corner-radius": 8,
    "background": "frost-surface",
    "border": { "width": 1, "color": "frost-slate" },
    "border-focused": { "width": 1, "color": "icy-blue" },
    "text-style": "body-large",
    "text-color": "frost-white",
    "placeholder-color": "frost-gray",
    "cursor-color": "icy-blue",
    "padding-horizontal": 16,
    "visual-transformation": "dot-masking for PIN, password fields"
  }
}
```

---

## Screen Layouts

### Splash Screen
- Full-screen `frost-background`
- Centered Tranzo logo (64dp)
- App name in `headline-medium` below logo (margin-top: 16dp)
- Loading indicator in `frost-cyan` near bottom (margin-bottom: 48dp)

### Onboarding Intro
- Full-screen `frost-background`
- Horizontal pager with 3 slides
- Each slide: illustration (200dp height), title (`headline-medium`), description (`body-large`, `frost-gray`)
- Page indicator dots at bottom (active: `icy-blue`, inactive: `frost-slate`)
- "Get Started" `FrostButtonPrimary` at bottom (margin: 16dp horizontal, 32dp bottom)

### Create Wallet
- `FrostAppBar` with back arrow and "Create Wallet" title
- Status indicator (step 1 of 3)
- Centered animation/illustration
- Description text (`body-large`)
- "Create New Wallet" `FrostButtonPrimary`
- "Import Existing Wallet" `FrostButtonSecondary` (margin-top: 12dp)

### Seed Phrase Backup
- `FrostAppBar` with "Backup Seed Phrase" title
- Warning banner (`frost-warning` background, `body-medium` text)
- 4x3 grid of word chips (each chip: `FrostCard`-style, `label-large`, numbered)
- "I have saved my seed phrase" `FrostButtonPrimary` at bottom

### PIN Setup
- `FrostAppBar` with "Set PIN" title
- Instruction text (`body-large`, centered)
- 6 PIN dots (filled: `icy-blue`, empty: `frost-slate`, size: 16dp, gap: 16dp)
- Numeric keypad (3x4 grid, each key: 64dp circle, `headline-medium` text, `frost-surface` background)
- Biometric icon option in bottom-left of keypad

### Wallet Home
- `FrostAppBar` with network selector and settings icon
- `WalletBalanceCard` (horizontal margin: 16dp, top margin: 8dp)
- "Your Tokens" section header (`title-large`, margin-top: 24dp)
- Scrollable list of `TokenListItem` components
- `FloatingSendButton` at bottom-right
- Bottom navigation bar: Home, Tokens, NFTs, Settings (icon + label, active: `icy-blue`, inactive: `frost-gray`)

### Send Token
- `FrostAppBar` with back arrow and "Send" title
- Token selector (current token with icon, tap to change)
- Recipient address `SecureInputField` with QR scan icon button
- Amount `SecureInputField` with "Max" text button
- `FrostCard` for fee summary: network fee, total, estimated time
- "Review" `FrostButtonPrimary`
- Confirmation modal (`FrostModalSheet`): recipient, amount, fee, "Confirm & Send" button with PIN/biometric gate

### Receive Token
- `FrostAppBar` with back arrow and "Receive" title
- `FrostCard` containing:
  - QR code (200dp, white foreground on frost-background)
  - Address text (`body-medium`, `frost-gray`, centered, truncated with "..." or full)
  - "Copy Address" `FrostButtonSecondary`
  - "Share" `FrostButtonSecondary`
- Network label (`caption`, `frost-gray`)

### Transaction History
- `FrostAppBar` with "Transactions" title
- Filter chips row: All, Sent, Received (active: `icy-blue` border, inactive: `frost-slate` border)
- Scrollable list of transaction items:
  - Icon (send: arrow-up in `frost-error`, receive: arrow-down in `frost-success`)
  - Address (truncated, `body-large`)
  - Timestamp (`caption`, `frost-gray`)
  - Amount (right-aligned, `body-large`, color by type)
  - Status badge (`caption`, colored by status)
- Empty state: illustration + "No transactions yet" text
