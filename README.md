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
* **Spec vs. Implementation:** During implementation, we introduced additional getter and setter methods in several classes, including TicketingModel and FanTicketRequest, that were not explicitly included in our original specification.
* **Prompt/Reason:** As we developed the model and connected it to the view, we found that additional access methods were necessary to maintain encapsulation while allowing the appropriate classes to retrieve and update information. We also expanded our test coverage to account for these methods. These additions improved communication between components without requiring us to compromise the overall architecture of the MVP.

### Model Independence
* The model does not directly depend on the view or require it to execute its core queue operations. We verified this separation through our JUnit tests, which exercise the model independently of the interface. Communication between the model and view is handled through the established interfaces and controller structure.

### Unknowns & Scope Status
* **Figured Out:** We figured out queue encapsulation with our singlyLinkedQueue class, it holds our basic methods and our 'service window' validates all the functions running without node leakage. The architecture we landed on for our MVP runs without issue in a logical manner and has created a viable solution to our prompt. Our questions with jUnit were resolved as we fleshed out our methods and added additional helper methods like getters or setters.
* **Still Open:** Object persistence, this becomes an issue as we begin to take on our first stretch goal, and thereafter. We will need further discussions on the best plan of action to hold data outside of our running program.

### Current Limitations / Known Issues
* The current MVP has no known blocking issues. During our initial testing, we deliberately attempted to trigger a variety of invalid-input and error conditions. The program recovered appropriately and continued operating without crashing. However, additional testing will be necessary as we expand the program's functionality. Object persistence also remains outside the scope of the current MVP and will need to be addressed as we implement future features.

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
Our MVP is complete, and no additional MVP functionality is currently outstanding. Our next priority is implementing the committed stretch goals.

Define the membership tier structure and establish the necessary classes and method signatures. [Francis and Kathy]

Implement the membership tier functionality, integrate it into the existing queue system, and develop corresponding tests. [Sebastian and Rafik]

Evaluate the implementation and determine whether merch pre-orders should be developed before or after membership tiers.


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
* isSilverTier() -- Same as above for silver
* isClassicTier() -- Same as above for classic
* enqueueGold() -- adds a fan to the gold queue (Good: adds a fan ; Bad: throws on a null or invalid input or mismatch tier type)
* dequeueGold() -- removes a fan from the gold queue (Good: removes a fan ; Bad: throws on an empty queue or invalid input or mismatch tier) 
* enqueueSilver() -- Same as above for silver
* dequeueSilver() -- Same as above for silver
* enqueueClassic() -- Same as above for classic
* dequeueClassic() -- Same as above for classic
* stepQueue() with all three tiers populated.
* stepQueue() with only one tier populated.
* stepQueue() with two tiers populated.
* Verify that the correct fan enters the purchase window according to your intended priority rules.

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
| `ConsoleQueueView.java` | Sebastian Pacheco | Wrote console-based view implementation to display menus, queue status, receipts, and error messages; implemented user input handling and validation. |
| `TicketingController.java` | Sebastian Pacheco | Wrote controller to coordinate user interactions between the view and model; implemented queue entry, line advancement, purchase confirmation, cancellation, and exception handling.|
| `SinglyLinkedQueueTest.java` | Kathryn Dedrick | Wrote test cases for the SinglyLinkedQueue class to assert all functions work without print statements/ use of a view |
| `TicketingModelTest.java` | Kathryn Dedrick | Wrote test cases for the TicketingModel class to assert all functions work without print statements/ use of a view. Gen-AI assisted |

### GenAI Usage Note
<!-- Describe specifically how Generative AI tools were used across the project, or explicitly write that Generative AI was not used. -->
GenAI was used to review the README against the project rubric and identify areas requiring clarification or additional detail. Several group members also used GenAI as an organizational and refinement tool when translating handwritten code into a cleaner implementation. All generated suggestions were reviewed and incorporated as appropriate by the group members responsible for the relevant code.
