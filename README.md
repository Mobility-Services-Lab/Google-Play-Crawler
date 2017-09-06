# Google-Play-Crawler
## Crawler for gathering app information from Googple Play
##### It adds data gathered from a given app page, then searches page for the links for similar apps and recursively gatheres data from them. 
# How-To
## Dependencies: 
##### org.apache.commons:commons-csv:1.4
##### com.opencsv:opencsv:4.0
##### org.jsoup:jsoup:1.10.3

## Runnable class: CrawlForApps

### Relevant variables:
##### boolean printResults - print out in console every app
##### boolean searchDeep - recursive search on/off
##### int Depth - how deep does it search recursively, beginning from 0=one of the initial apps
##### int maxAppsToGatherAtATime - stop search after searching through this many pages
##### String CSVName - name of .CSV file to load from/ save to

### main():
##### parse four pages (with recursive deep search)
##### runTheMapThrough(Data) - method to parse all the pages that are already in db, but were not searched recursively. For searching deeper/farther from 4 initial apps.
