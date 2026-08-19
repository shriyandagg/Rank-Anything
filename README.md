# RankAnything

A Java application that creates personalized rankings through pairwise comparisons using a binary-search insertion algorithm.

---

## Overview

RankAnything allows users to build personalized rankings by comparing two items at a time instead of assigning arbitrary scores. The application uses binary-search insertion to efficiently determine where new items belong while minimizing the number of comparisons required. The project was built to strengthen object-oriented programming, algorithm design, file persistence, and search functionality while creating a practical ranking system.

---

## Features

- Pairwise comparison ranking
- Binary-search insertion
- Automatic rating assignment
- Search with typo correction
- Edit, delete, and rerank items
- Persistent file storage
- Ranking statistics

---

## Algorithms

RankAnything uses **binary-search insertion** to minimize the number of comparisons required when inserting a new item.

After an initial training phase, the application estimates ratings for newly added items based on their position relative to neighboring rankings.

The application also uses **Levenshtein Distance** to provide typo-tolerant search suggestions.

---

## Main Menu

The application provides a simple console interface for viewing rankings, adding new items, searching, editing entries, and viewing statistics.

```text
==================================
            RankAnything
==================================
1. View Rankings
2. Add Item
3. Delete Item
4. Search Item
5. Edit Item
6. View Statistics
7. Exit
==================================
```

---

## Ranking a New Item

When a new item is added, RankAnything compares it against existing entries using pairwise comparisons and a binary-search insertion algorithm. After the correct position is found, the application estimates an initial rating based on nearby items.

```text
Which do you prefer?

1. Frieren: Beyond Journey's End
2. Gurren Lagann

Enter 1 or 2: 1

Which do you prefer?

1. Frieren: Beyond Journey's End
2. Naruto

Enter 1 or 2: 2

...

Frieren: Beyond Journey's End was placed at rank #7.

Nearby rankings:
#6 Hunter x Hunter — 9.0/10
#7 Frieren: Beyond Journey's End — Not rated yet
#8 Tomodachi Game — 8.8/10

Based on its ranking position, Frieren: Beyond Journey's End received a rating of 8.8/10.
```

---

## Searching the Rankings

Users can quickly search for any ranked item to view its current ranking and rating. If a search term is misspelled, the application suggests the closest matching item using Levenshtein Distance.

```text
Search Result

Item: Frieren: Beyond Journey's End
Current Rank: #7
Rating: 8.8/10
```

---

## Technologies

- Java
- Object-Oriented Programming
- ArrayLists
- File I/O
- Binary Search
- Levenshtein Distance

---

## Algorithms

- Binary search is used to efficiently determine the insertion position for new items, requiring O(log n) comparisons.
- Rankings are stored in an ArrayList, so inserting an item requires shifting later elements, resulting in O(n) overall insertion time.
- Levenshtein Distance is used to provide typo-tolerant search suggestions.
---

## Running the Project

Clone the repository and compile the Java source files.

```bash
javac Main.java
java Main
```

---

## Future Improvements

- Support multiple ranking lists
- GUI version (JavaFX)
- Export rankings to CSV or PDF
- Undo/redo functionality
- Web-based version

---

## What I Learned

While building RankAnything, I gained experience with:

- Object-oriented design
- Binary search algorithms
- File persistence
- Efficient data management
- Search algorithms and typo correction
- Building a complete console application from design to implementation
