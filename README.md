# RankAnything

A Java application that allows users to rank anything using pairwise comparisons instead of assigning ratings directly.

Instead of deciding a numerical score, users compare two items at a time. The application automatically determines where each new item belongs in the rankings and saves everything between sessions.

---

## Features

- View current rankings
- Add new items through comparison-based ranking
- Binary-search insertion to minimize comparisons
- Search for items
- Delete items
- Prevent duplicate entries
- Automatically save and load rankings

---

## How It Works

When a new item is added, the program compares it against existing items.

Example:

Which do you prefer?

1. Jujutsu Kaisen
2. Vinland Saga

↓

Which do you prefer?

1. Jujutsu Kaisen
2. Demon Slayer

↓

The application determines the correct ranking position based on your answers.

---

## Technologies

- Java
- Object-Oriented Programming
- ArrayLists
- File I/O
- Git
- GitHub

---

## Future Features

- Manual ratings
- Suggested ratings based on comparison history
- Confidence score for each ranking
- "Did you mean...?" typo detection
- Statistics dashboard
- Export rankings
- Categories (Anime, Movies, Restaurants, Games, etc.)
- Suggestion mode that recommends common items the user may have forgotten to add.

---

## Why I Built This

I wanted a better way to rank subjective things.

Giving every item a score from 1–10 often leads to inconsistent ratings. Instead, this project uses pairwise comparisons to build rankings naturally while minimizing the number of comparisons required.

The goal is to create a generic ranking engine that can be used for anime, movies, books, restaurants, games, or anything else.
