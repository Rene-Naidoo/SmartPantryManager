# Smart Pantry Manager

A Java Android application that helps reduce food waste. The user tracks the ingredients they already have at home (their "pantry"), and the app suggests only the recipes they can cook **right now**, with no shopping trip required.

**Author:** Rene Naidoo

## Core Feature: The Strict-Matching Rule

A recipe is suggested only if **every** ingredient it needs is in the pantry, in at least the required quantity.
00000
- A recipe needing 5 ingredients when the pantry has 4 is **not** shown.
- Ingredient names are normalised (case, spacing, plurals), so "Tomatoes" matches "tomato".
- Units are converted to a base unit (kg to g, l to ml, tsp and tbsp to ml), so 1 kg of flour satisfies a recipe needing 150 g.
- Duplicate pantry rows are added together.

The logic lives in `logic/RecipeMatcher.java` and is covered by 8 unit tests in `RecipeMatcherTest.java`.

## Features

- Pantry management: add, edit and delete ingredients (name, quantity, unit, optional expiry date)
- Pantry list screen using a RecyclerView with a custom Adapter
- 18 pre-loaded recipes, seeded on first run
- Suggested Recipes screen showing only strict matches, with a friendly message when none match
- Recipe Detail screen with the full ingredient list and method
- Settings screen with an expiring-soon highlight toggle (saved in SharedPreferences)
- Input validation on the ingredient form
- Bottom navigation between Pantry, Recipes and Settings

## Database: SQLite with Room

I chose **SQLite using the Room persistence library**, implemented locally on the device. Reasons:

1. The app works fully offline, which suits a pantry tool.
2. It needs no backend, account or server, unlike Firebase or PostgreSQL.
3. Room checks SQL queries at compile time, so mistakes fail the build instead of crashing the app.
4. The data is structured and related (recipes have many ingredients), which suits tables with a foreign key.

Data is stored in `smart_pantry.db` on the device, so it persists after the app is closed and reopened.

| Table | Purpose |
|---|---|
| `pantry_items` | The user's current ingredients |
| `recipes` | Recipe name and method |
| `recipe_ingredients` | Ingredient lines linked to a recipe by `recipeId` |

## Tech Stack

- Language: **Java** (the Gradle build scripts use Kotlin DSL, which is Android Studio's default; no app code is Kotlin)
- IDE: Android Studio
- Database: SQLite via Room 2.6.1
- UI: XML layouts, Fragments, RecyclerView, Material Components
- Minimum SDK: API 24 (Android 7.0)

## Screens

| Screen | Type |
|---|---|
| Pantry list | Fragment (tab 1) |
| Add / Edit ingredient | Activity (opened with an Intent) |
| Suggested recipes | Fragment (tab 2) |
| Recipe detail | Activity (opened with an Intent) |
| Settings | Fragment (tab 3) |

## Setup and Run Instructions

1. Install **Android Studio** (a recent version).
2. Clone the repository:
   ```
   git clone https://github.com/Rene-Naidoo/SmartPantryManager.git
   ```
3. In Android Studio choose **File > Open** and select the cloned folder.
4. Wait for Gradle to finish syncing.
5. Connect an Android phone with **USB debugging** enabled, or start an emulator.
6. Select the device and press the green **Run** button.

The 18 recipes are inserted automatically the first time the app launches.

## Testing the Strict-Matching Rule

1. Open the Recipes tab with an empty pantry. A "No recipes match" message appears.
2. Add: egg 3 pcs, tomato 1 pcs, butter 10 g, salt 1 tsp. **Tomato Omelette** appears.
3. Delete the salt (or reduce the butter). The omelette disappears.

Unit tests: right-click `RecipeMatcherTest` and choose **Run**.

## Out of Scope

No Google Maps, mapping SDKs, GPS or location features are used.