# CS-2114-Project-3-Group-17-

## Reflection

<!--
Instructions based on rubric:
- What changed from the spec (classes, signatures, validation, tests):
  * Detail what the spec originally said, what you actually built, and why.
  * (Exceeds): Trace each change directly to what prompted it.
- Whether the model really stood alone.
- What happened to the unknowns from the scope:
  * Clarify what you figured out vs. what is still open.
- What doesn't work yet.
-->

### Spec Deviations & What Prompted Them
* **Spec vs. Implementation:** 
* **Prompt/Reason:** 

### Model Independence
* 

### Unknowns & Scope Status
* **Figured Out:** 
* **Still Open:** 

### Current Limitations / Known Issues
* 

---

## Next Steps

<!--
Instructions based on rubric:
- What's left to finish the MVP, in priority/chronological order.
- Name one non-trivial change/feature not in the MVP:
  * Predict which classes and specific methods it will touch. (Exceeds: Name specific methods, not just classes).
  * Predict which classes should remain unchanged.
- Stretch goals: Committed vs. Dropped.
- New tests needed.
- Explicit owner assigned for each remaining item.
-->

### Remaining Work for MVP (In Order)
1. **[Task Name]** - Owner: `[Name]`
2. **[Task Name]** - Owner: `[Name]`

### Future Non-Trivial Feature (Post-MVP)
* **Feature Description:** 
* **Affected Classes & Specific Methods:** 
* **Classes That Should Not Change:** 

### Stretch Goals
* **Committed:** 
* **Dropped:** 

### New Tests Needed
* 

---

## Attribution & GenAI Note

<!--
Instructions based on rubric:
- Table with one row per class and test file.
- Each row lists who worked on it and how (wrote, revised, reviewed, tested, GenAI-assisted, etc.).
- GenAI note detailing how Generative AI was used, or explicitly stating that it was not used.
-->

| File / Class | Contributors | Role / Contribution (Wrote, Revised, Reviewed, Tested, GenAI-Assisted) |
| :--- | :--- | :--- |
| `Main.java` | Francis Kwon | Wrote the entry-point wiring Model, View, and Controller for application execution.|
| `QueueNode.java` | Francis Kwon | Wrote node implementation with generic data holder and next pointer reference. |
| `QueueObserver.java` | Francis Kwon |Defined decoupled observer interface contract (`update` and `showError`) for push notifications. |
| `QueueStatusDTO.java` | Francis Kwon | Wrote immutable DTO class carrying metrics across Model/View boundary. |
| `SinglyLinkedQueue.java` | Francis Kwon | Wrote custom singly linked FIFO queue; implemented head/tail pointer mechanics and O(1) operations. |
| `TicketingModel.java` | Kathryn Dedrick | |
| `FanTicketRequest.java` | Rafik Khismatov | |
| `QueueView.java` | Rafik Khismatov  | |
| `ConsoleQueueView.java` | | |
| `TicketingController.java` | | |
| `SinglyLinkedQueueTest.java` | Kathryn Dedrick | Wrote test cases for the SinglyLinkedQueue class to assert all functions work without print statements/ use of a view |
| `TicketingModelTest.java` | Kathryn Dedrick | Wrote test cases for the TicketingModel class to assert all functions work without print statements/ use of a view |

### GenAI Usage Note
<!-- Describe specifically how Generative AI tools were used across the project, or explicitly write that Generative AI was not used. -->
