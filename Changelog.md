
# Change Log
All notable changes to this project from week 7 onwards will be documented in this file.
 
## [Unreleased] - 2021-06-03

Worked mainly on upgrading filter/search capabilities of this application.
 
### Added
Added additional search functionality:
- advanced search: sender (email address)
- advanced search: receiver (email address)
- advanced search: start date 
- advanced search: end date
- advanced search: has attachment
  
### Changed
- search/filter function can now be called with search button and enter key.
 
### Fixed

## [Unreleased] - 2021-06-09
 
After the added filtering capabilities we decided to start differentiating between Kick-In members and Association members.
Additionaly document preview was created with the option to download the file. 

### Added
- two html files to replace one file where differentiation was needed:
  - main overview page
  - email preview
### Changed
- Styling of the advanced drop down filter
 
### Fixed
 
## [Unreleased] - 2021-06-16
 
Added styling to make the pages scalable for usage of larger and smaller screen sizes. Also, parts of OAuth 2.0 were being implemented.
 
### Added
- credentials being replaced by a clickable user icon when in smaller screen sizes
- buttons moving to the center and not dissapearing off screen
### Changed
- document preview has a refresh button
 
### Fixed
- when document preview was refreshed in email preview, the back button had to be clicked as many times as the document was refreshed. 

## [Unreleased] - 2021-06-18
OAuth 2.0 login functionality is implemented and now users are checked if they are allowed to access the email/document they try to refer to. 
 
### Added
- login OAuth 2.0 functionality
- permission checks for email and document with access key info
 
### Changed
 
### Fixed

## [Unreleased] - 2021-06-22

When association users access an email or document information of them accessing that is stored in the database and accesible by kick-in members. 
 
### Added
- mark who has read an email/document
- check who has read an email/document
 
### Changed
- logout OAuth 2.0 funtionality
 
### Fixed

## [Unreleased] - 2021-06-25

Testing and fixing bugs 
 
### Added
 
### Changed
- logout OAuth 2.0 funtionality
 
### Fixed
- When refresh page if user first clicks button next, the next page would show no results and the showing results would be incorrect

## [Unreleased] - 2021-06-26

Adding the ability to add new emails and document. Also users search information retains when selecting an email.
 
### Added
- create/upload new email/document
 
### Changed
- when back the button is clicked, the user is redirected to page with their previous results from the search.
 
### Fixed
