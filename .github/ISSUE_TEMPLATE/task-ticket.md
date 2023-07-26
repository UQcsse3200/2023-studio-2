---
name: Task Ticket
about: Description of a task as part of implementing a feature.
title: ''
labels: task
assignees: ''

---

# **Description**
**Task:** [Task Name](#3).
**Feature:** [Feature which this tasks contributes to implementing](#2)

A one paragraph description of the task on which you will work. It should briefly explain what will be done (e.g. "Implement an armour component"). This includes textual explanations of the work. (e.g. "`Armour` inherits from `Component` and will add an event handler to the player entity so that the armour takes damage rather than the player. Different types of armour will have different durability. Once the armour is broken (durability reaches zero), it will no longer take damage and more attacks on the player will cause it to lose health.")
 
## **Example (optional)**
- **Part**: Depending on the task, you may want to provide some examples of how it would play out in the game. See next point for example...
- **Armour Benefits**: Players can wear different classes of armour. Armour takes damage until it breaks. Armour cannot be healed but can be replaced. If a player is wearing armour, its health is not affected when it takes damage unless the armour is broken.

# **Dependencies**
List any tasks on which this ticket relies. If there is already an existing ticket, reference it using #xx. If a task needs to be implemented, and the issue does not already exist, make a new ticket and leave it unassigned for someone else to take on. Ensure other team members are aware of the issue.

- [ ] Dependency 1 (#xx)
- [ ] Dependency 2 (#xx)

e.g.
- [ ] Texture needs to be created for armour laying on map.
- [ ] Texture needs to be created for player wearing armour.
- [ ] Textures need to be created for partially, and then fully, broken armour.
- [ ] Mechanism needs to be implemented for armour to be placed onto map.
 
# **Milestones**
List of steps that need to be completed for this task.
- [ ] Step 1 (Aug. 19)
- [ ] Step 2 (Aug. 20)
- [ ] Step 3 (Aug. 23)
- [ ] Step 4 (Aug. 23)
 
**Completion Deadline:** Aug. 23

e.g.
- [ ] Initial design of `Armour` created and documented on wiki (Aug. 19).
- [ ] Unit tests implemented for `Armour` (Aug. 20).
- [ ] `Armour` class implemented and tested (Aug. 23).
- [ ] Documentation of `Armour` updated on wiki (Aug. 23).
 
# **Documentation**
- [Main description of feature](../wiki/Game-Screens)
- [JavaDoc](JavaDoc/Location/Package/Class)
- ...
 
# **Member**
- Name (GitHub Username) (Slack Handle)
- e.g. Richard (@AppleByter) (Richard Thomas)
