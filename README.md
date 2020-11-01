# bibtexUtils
Utilities for BibTeX files.

# Dependencies 
* jbibtex library (https://github.com/jbibtex/jbibtex)
* jackson-annotations
* jackson-databind

# Aim
The aim of the project is twofold:
(1) Test the jbibtex library to manage BibTeX files
(2) Create a set of utilities to operate with BibTeX files.


# Utilities

1. BibTeX diff, given two BibTex files bib1 and bib2, creates a new BibTeX file with the entries that are in bib2 but no in bib1.
