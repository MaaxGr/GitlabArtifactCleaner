# Gitlab Artifact Cleaner

Project to clean old gitlab artifacts!

# Key-Idea
* Access gitlab jobs and corresponding artifacts via gitlab api 
* Cache all jobs in a local database (MySQL atm) to prevent flooding the gitlab api all the time
* HTTP Endpoint to analyze a project (lists old jobs and estimates size of artifacts that can be deleted)

# Default retain policy
* Keep artifacts of last X days
* Keep the last artifact of each ref


# Roadmap
## First Release
* [x] Create base project structure
* [x] Create database entities
* [x] Create gitlab integration
* [x] Write database update logic
* [x] Write analyze logic
* [ ] Create endpoint that actually deletes artifacts
* [ ] Authentication for API

## Future Releases
* [ ] make retain policy configurable
* [ ] create web ui
