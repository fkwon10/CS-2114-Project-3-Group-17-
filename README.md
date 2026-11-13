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
* **Spec vs. Implementation:** Additional defining methods in various classes, including TicketingModel, FanTicketRequest, and a few others gaining getter and setter methods not previously listed.
* **Prompt/Reason:** We realized as we were writing and debugging it was necessary to have these methods for access in our view and model, and we added test cases as needed to each additional method. Overall it went smoother after we implemented the small quality of life methods.

### Model Independence
* The model does run without its view, we were able to verify each with the jUnit tests.

### Unknowns & Scope Status
* **Figured Out:** We figured out queue encapsulation with our singlyLinkedQueue class, it holds our basic methods and our 'service window' validates all the functions running without node leakage. The architecture we landed on for our MVP runs without issue in a logical manner and has created a viable solution to our prompt. Our questions with jUnit were resolved as we fleshed out our methods and added additional helper methods like getters or setters.
* **Still Open:** Object persistence, this becomes an issue as we begin to take on our first stretch goal, and thereafter. We will need further discussions on the best plan of action to hold data outside of our running program.

### Current Limitations / Known Issues
* None at this time, more testing will be required. In the first 15 minutes of our MVP running, we attempted to hit every error we could; the program resumed smoothly each time.

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
Our MVP is complete. 
We should move on to implementing our stretch goals.
1. Definitions and classes [Francis and Kathy]
2. Implementations and testing [Sebastian and Rafik]


### Future Non-Trivial Feature (Post-MVP)
* **Feature Description:** We planned to implement three membership tiers, gold, silver and classic offering different perks in our main ticket model queue.
* **Affected Classes & Specific Methods:** Our view classes, queue system, and most importantly our stepQueue method would be the most impacted. Our plan was to create several different queues resulting in a different rotation into the purchase window and a new series of checks within the method. Another affected class would be the FanTicketRequest because it would need to take the tier into consideration during it's constructor.
* **Classes That Should Not Change:** The SinglyLinkedQueue will remain the same as the basic function for our queue.

### Stretch Goals
* **Committed:** Membership Tiers, and Merch Pre-order. The merch pre-order may be implemented after tiers or before, depending on how the first implementation of stretch goals runs.
* **Dropped:** Seat Upgrade Roulette

### New Tests Needed
* getTier() -- get's the fan's tier (Good: returns tier ; bad: throws on an invalid fan)
* isGoldTier() -- boolean to assert which queue the fan is added to (Good: returns true on a gold fan ; Bad: throws on an invalid fan)
* isSilverTier() -- Same as above
* isClassicTier() -- Same as above
* enqueueGold() -- adds a fan to the gold queue (Good: adds a fan ; Bad: throws on a null or invalid input or mismatch tier type)
* dequeueGold() -- removes a fan from the gold queue (Good: removes a fan ; Bad: throws on an empty queue or invalid input or mismatch tier) 
* enqueueSilver() -- Same as above
* dequeueSilver() -- Same as above
* enqueueClassic() -- Same as above
* dequeueClassive() -- Same as above

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
| `TicketingModel.java` | Kathryn Dedrick | Wrote ticketing methods to validate the ability to use a service view model.|
| `FanTicketRequest.java` | Rafik Khismatov | Wrote immutable request record storing a fan's name, ticket quantity, and join timestamp; added constructor validation, equals/hashCode, and toString. |
| `QueueView.java` | Rafik Khismatov  | Defined view interface contract for menu, input prompts, checkout decisions, receipts, and sold-out notices using only primitives and Strings so the view stays decoupled from the model. GenAI-assisted. |
| `ConsoleQueueView.java` | Sebastian Pacheco | |
| `TicketingController.java` | Sebastian Pacheco | |
| `SinglyLinkedQueueTest.java` | Kathryn Dedrick | Wrote test cases for the SinglyLinkedQueue class to assert all functions work without print statements/ use of a view |
| `TicketingModelTest.java` | Kathryn Dedrick | Wrote test cases for the TicketingModel class to assert all functions work without print statements/ use of a view. Gen-AI assisted |

### GenAI Usage Note
<!-- Describe specifically how Generative AI tools were used across the project, or explicitly write that Generative AI was not used. -->
GenAI was used to assert the README followed the rubric. GenAI was used by several group members to organize our own handwritten code into a cleaner and more efficient program. 
