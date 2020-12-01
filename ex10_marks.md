# Total:  / 25 (4)
    
# Requirements implemented (15)
Document the requirements implemented in the file called `deliverables.md`. A completed requirement is one where the TA can execute the feature (e.g., add whales) and it works as expected, does not generate exceptions, and has associated tests.

- application does not compile and tests do not all pass (-5)
- missing one requirement (-2)
- missing two requirements (-5)
- missing three requirements (-10)
- missing 4 or more (0 marks)
- ADRs missing, hard to understand or lack insight (-2 per)
- tests do not cover key functionality (-2)
- tests lack detail, e.g., only cover simple cases (-2) 
- satisfaction of requirement is not well documented or requires complex workarounds and back and forth with TA (-2 per requirement)

# Code correctness and understanding of Java, Play, and course concepts (0-5)
- [ ] 5 - All code well written and commented. Unit tests exist. SBT file works. Basic OO principles are followed. Play web application structure and architecture clear and easy to follow.
- [ ] 4 - Structure confusing, code hard to follow in places. Code otherwise clean and well-written. A 4 may not adhere to MVC principles (if you do not - explain in an ADR!)
- [ ] 3 - Code is acceptable in most places, but OO issues like duplicated code, encapsulation violations. Lacks comprehension of MVC pattern and web apps. 
- [ ] 2 - Code does not compile or not possible to run. Missing many aspects; lacks insight and evidence of work.
- [ ] 1 - Does not compile. Little evidence of work; mostly missing components.
- [ ] 0 - not handed in or not available. 

# Look and feel (0-5)
- [ ] 5 - amazing UI, no bugs, something that could be shown to clients.
- [ ] 4 - expected solution, well put together.
- [ ] 3 - missing some key components, lacks usability, throws exceptions or errors.
- [ ] 2 - missing many aspects; lacks insight and work.
- [ ] 1 - little evidence of design; mostly missing components.
- [ ] 0 - not handed in or not available.

# Extra Features (up to 4 bonus marks)
You get bonus marks if you attempt and succeed at more complex apps. Note that your mark depends on completing the feature; merely showing how Selenium can be used to test one aspect of the app is not sufficient. Bonus work typically takes a substantial effort.

0 bonus marks for the bare minimum requirements. Two additional marks for each feature attempted and tested, max 2 (4 marks total).
    
- GUI testing (eg. with [Selenium](https://www.selenium.dev/))
- Persist the data to a data store, such as Firebase, H2 (in memory), or other. Ensures the app's data is maintained after the app is exited.
- Client-side javascript that does form validation or generates UI tweaks (maps, charts, etc.).
- Hosting the app on Google, AWS, or other remote sites, so the TA can access directly via a URL you distribute.
- Implement [logging](https://www.playframework.com/documentation/2.8.x/JavaLogging) for all operations.