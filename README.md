# CS-2114-Project-3-Group-17- — Encore Concert Ticketing Queue

Encore is an object-oriented Java concert ticketing queue management system built with a strict Model-View-Controller (MVC) and push-based Observer architecture.

---

## How to Compile and Run

### Compile the Application and Tests
From the project root directory (`CS-2114-Project-3-Group-17-`), compile all source and test files into `bin/` using JUnit 4 (`lib/junit-4.13.2.jar` and `lib/hamcrest-core-1.3.jar`):

```bash
mkdir -p bin
javac -cp "lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" -d bin src/encore/*.java src/encore/model/*.java src/encore/view/*.java src/encore/controller/*.java
```

### Run the Interactive Console Application
```bash
java -cp bin encore.Main
```

### Run the JUnit 4 Test Suite
```bash
java -cp "bin:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore encore.model.SinglyLinkedQueueTest encore.model.TicketingModelTest
```

---

## Reflection

### How Our Design Absorbed the Change (Touched Classes vs. D3 Prediction)
* **Change Feature Implemented:** Three Membership Tiers (`GOLD`, `SILVER`, and `CLASSIC`) offering distinct ticket limits and queue perks (`CLASSIC`: 1–4 tickets; `SILVER`: 1–6 tickets; `GOLD`: 1–8 tickets plus instant VIP Fast-Track directly to the checkout window when the window is free and the line is empty).
* **Classes Actually Touched:**
  * `TicketingModel.java` (`customerTiers` map, `getMaxTicketsForTier`, overloaded `enqueueFan(String, int, String)`, `getCustomerTier`, and tier cleanup on purchase/cancellation)
  * `FanTicketRequest.java` (relaxed constructor upper bound from `4` to `8` so `SILVER` and `GOLD` requests can be instantiated while `TicketingModel` enforces each tier's specific cap)
  * `QueueView.java` (`promptMembershipTier` interface method)
  * `ConsoleQueueView.java` (`promptMembershipTier` console input prompt)
  * `TicketingController.java` (updated `case 1` in `run()` and `onJoinQueue(String, int, String)`)
  * `TicketingModelTest.java` (`testMembershipTiersNormalPerks` and `testMembershipTiersBadInput`)
* **What Matched vs. What Surprised Us:**
  * **Matched:** Our Deliverable 3 plan explicitly predicted that `SinglyLinkedQueue` would remain completely unchanged as our underlying FIFO structure, and that `FanTicketRequest` and the view/controller layers would need updates to support tier input. Indeed, `SinglyLinkedQueue.java`, `QueueNode.java`, `QueueObserver.java`, `QueueStatusDTO.java`, and `Main.java` required zero modifications.
  * **Surprised Us:** In D3, we anticipated creating three separate queues and rewriting `stepQueue()` with multi-queue rotation checks (`enqueueGold`, `enqueueSilver`, `enqueueClassic`). During implementation, we discovered we could keep a single `SinglyLinkedQueue<FanTicketRequest>` and leave `stepQueue()` untouched by storing active fan tiers in a `HashMap<String, String>` inside `TicketingModel` and handling `GOLD` fast-tracking in `enqueueFan(...)`. We were also surprised when `FanTicketRequest`'s hardcoded `1–4` constructor validation rejected `GOLD` 8-ticket orders during unit testing, prompting us to raise its ceiling to `8`.

### Did the Change Cross the Model/View Line?
* No—the change respected our MVC boundary and did not leak model internals into the view. `ConsoleQueueView` holds zero copy of model state and knows nothing about tier ticket caps (`4`, `6`, `8`), `HashMap` storage, or VIP fast-track rules. The view simply prompts for a raw `String` (`promptMembershipTier()`) and passes it to `TicketingController`, which delegates all validation, tier normalization, and fast-track logic to `TicketingModel`.

### What Changed from Our D3 Plan & Why
* **Single Queue + `customerTiers` Map Instead of Three Separate Queues:** Instead of splitting `TicketingModel` into three queues with `isGoldTier`/`enqueueGold`/`dequeueGold` methods, we implemented `getMaxTicketsForTier(String)`, `getCustomerTier(String)`, and overloaded `enqueueFan(String, int, String)` while keeping `enqueueFan(String, int)` defaulting to `"CLASSIC"`. This preserved backward compatibility with every existing D3 test and kept `stepQueue()` clean.
* **Package Structure & Signature Alignment:** We organized all source and test files into `encore.model`, `encore.view`, and `encore.controller` packages and aligned `TicketingController` calls to the `QueueView` interface (`promptCustomerName`, `promptTicketQuantity`, `readMenuChoice`, 3-argument `showReceipt`).
* **Design Change That Would Make the Next Feature Cheaper:** Replacing raw `String` tier identifiers (`"GOLD"`, `"SILVER"`, `"CLASSIC"`) with a dedicated `MembershipTier` enum (encapsulating `maxTickets`, `fastTrackEligible`, and `discountRate`) would eliminate string normalization and `switch` branching in `TicketingModel`, making our next feature (Merch Pre-Orders or tier-based discounts) a single enum property lookup rather than modifying conditional logic across methods.

### What Doesn't Work Yet
* **Mid-Queue Priority Insertion for `GOLD` Members:** Currently, a `GOLD` fan fast-tracks straight to the checkout window when the window is open and the line is empty, but if a queue has already formed, they enter at the tail rather than ahead of `SILVER`/`CLASSIC` fans.
* **Merch Pre-Order Add-On & Automated Sold-Out Broadcast:** Our second committed stretch goal (Merch Pre-Orders) is not yet implemented, and `TicketingController` does not yet automatically broadcast `QueueView.showSoldOut(int fansStillWaiting)` the moment `ticketsRemaining` hits `0`.

---

## Next Steps

### Remaining Work for Final Build (In Order)
1. **Refactor Tiers to a `MembershipTier` Enum & Support Mid-Line `GOLD` Priority:** Replace `String` tier checks in `TicketingModel` with a `MembershipTier` enum and allow `GOLD` members to jump ahead of non-`GOLD` fans when joining a non-empty queue. — **Owner:** `Kathryn Dedrick`
2. **Implement Merch Pre-Order Stretch Goal(If we have time):** Add optional concert merchandise bundle selection during checkout (`FanTicketRequest` / `TicketingModel`) and display merch items on the receipt. — **Owner:** `Rafik Khismatov`
3. **Trigger Automated Sold-Out Broadcast & Controller Tests(If we have time):** Invoke `view.showSoldOut(model.getQueueSize())` automatically in `TicketingController` when inventory reaches `0`, and add stub-view JUnit tests for `TicketingController`. — **Owner:** `Sebastian Pacheco`
4. **Final End-to-End Integration, JavaDoc & Demo Rehearsal:** Verify full-system test coverage, clean repository state, and timed presentation pacing. — **Owner:** `Francis Kwon`

### Stretch Goals: Committed vs. Dropped
* **Committed:** Membership Tiers (`GOLD`, `SILVER`, `CLASSIC` — core implemented in D4, enum/priority refinement in final build) and Merch Pre-Orders.
* **Dropped:** Seat Upgrade Roulette and file-based object persistence (CSV/JSON), as both introduce randomness/file I/O outside our core deterministic queue and MVC learning goals.

### 3-Minute Demo Walkthrough
* **0:00 – 0:35 (Architecture & MVC Separation):** Run `encore.Main` (initialized with 10 tickets) and briefly show our `encore.model`, `encore.view`, and `encore.controller` packages, highlighting that `ConsoleQueueView` holds zero model state and updates via `QueueStatusDTO`.
* **0:35 – 1:25 (Membership Tiers & Perks):** Join `Alice` as **`GOLD`** for `6` tickets—demonstrate her higher `8`-ticket limit and instant VIP fast-track straight to checkout, then confirm her purchase (`4` tickets left). Next, join `Bob` as **`SILVER`** (`5` tickets) and `Charlie` as **`CLASSIC`** (`3` tickets), showing both enter the FIFO queue.
* **1:25 – 2:10 (Bad-Input Survival & Checkout Flow):** Enter invalid inputs—`CLASSIC` requesting `5` tickets (exceeds 4-ticket cap), an invalid tier `PLATINUM`, duplicate username `Bob`, and non-numeric menu input `abc`—demonstrating graceful error messages with zero crashes. Advance `Bob` (`5` requested, `4` remaining) and demonstrate partial-stock confirmation.
* **2:10 – 3:00 (JUnit 4 Model Tests & Reflection):** Run `SinglyLinkedQueueTest` and `TicketingModelTest` in VS Code's Test Runner, showing all 11 model-isolated tests passing (including `GOLD`/`SILVER`/`CLASSIC` tier bounds and interleaved FIFO queue ordering).

---

## Attribution & GenAI Note

| File / Class | Contributors | Role / Contribution (Wrote, Revised, Reviewed, Tested, GenAI-Assisted) |
| :--- | :--- | :--- |
| `Main.java` | Francis Kwon | Wrote the entry-point wiring Model, View, and Controller for application execution; revised package imports. |
| `QueueNode.java` | Francis Kwon | Wrote node implementation with generic data holder and next pointer reference. |
| `QueueObserver.java` | Francis Kwon | Defined decoupled observer interface contract (`update` and `showError`) for push notifications. |
| `QueueStatusDTO.java` | Francis Kwon | Wrote immutable DTO class carrying metrics across Model/View boundary. |
| `SinglyLinkedQueue.java` | Francis Kwon | Wrote custom singly linked FIFO queue; implemented head/tail pointer mechanics and O(1) operations. |
| `TicketingModel.java` | Kathryn Dedrick, Francis Kwon | Kathryn wrote core ticketing methods to validate the service view model; Francis implemented `GOLD`/`SILVER`/`CLASSIC` membership tier perks and validation. |
| `FanTicketRequest.java` | Rafik Khismatov, Francis Kwon | Rafik wrote immutable request record storing a fan's name, ticket quantity, and join timestamp with constructor validation, `equals`/`hashCode`, and `toString`; Francis updated quantity ceiling to `8` for `GOLD` tier support. |
| `QueueView.java` | Rafik Khismatov, Francis Kwon | Rafik defined view interface contract for menu, input prompts, checkout decisions, receipts, and sold-out notices using only primitives and Strings; Francis added `showStatus` and `promptMembershipTier`. GenAI-assisted. |
| `ConsoleQueueView.java` | Sebastian Pacheco, Francis Kwon | Sebastian wrote console-based view implementation to display menus, queue status, receipts, and error messages with input validation; Francis added `promptMembershipTier`. |
| `TicketingController.java` | Sebastian Pacheco, Francis Kwon | Sebastian wrote controller to coordinate user interactions between the view and model; Francis aligned method signatures to `QueueView` and wired membership tier routing. GenAI-assisted. |
| `SinglyLinkedQueueTest.java` | Kathryn Dedrick, Francis Kwon | Kathryn wrote test cases for `SinglyLinkedQueue` to assert all functions work without print statements or a view; Francis added `testInterleavedEnqueueDequeueOrder`. |
| `TicketingModelTest.java` | Kathryn Dedrick, Francis Kwon | Kathryn wrote test cases for `TicketingModel` to assert all functions work without print statements or a view; Francis added `GOLD`/`SILVER`/`CLASSIC` tier normal and bad-input tests. GenAI-assisted. |

### GenAI Usage Note
GenAI was used to review the README against the project rubric and identify areas requiring clarification or additional detail. Several group members also used GenAI as an organizational and refinement tool when translating handwritten code into a cleaner implementation, resolving package/classpath configuration issues, aligning `QueueView` and `TicketingController` method signatures, and drafting edge-case JUnit 4 tests (interleaved queue ordering and membership tier bounds). All generated suggestions and tests were reviewed, executed, and verified by the group members responsible for the relevant code.
