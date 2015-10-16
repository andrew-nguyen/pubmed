# pubmed

A Clojure library for simple queries on pubmed entrez eutils interface.

## Usage

### Search for titles of papers about microRNAs and biomarkers in breast cancer.

#### Query the IDs
```clojure

(def res (id-search "\"MicroRNAs\"[nm] AND \"Tumor Markers, Biological\"[nm] AND \"Breast Neoplasms/genetics\"[MAJR]"))

(id-list res)

-> ["26124344" "26026077" "25883093" "25742469" "25640367" "25571912" "25474246" "25465851" "25448984" "25445205" "25337203" "25277099" "25232827" "25213696" "25195131" "25120807" "25107641" "25086636" "25051376" "25048467"]

```
#### Fetch summaries for the IDs

```clojure

(def summ-res (summary (id-list res)))

(take 3 (summary-title-dt-id summ-res))

->  (["Quantitative assessment of miR34a as an independent prognostic marker in breast cancer." "2014 Dec 4" "25474246"]
     ["microRNA Expression in Prospectively Collected Blood as a Potential Biomarker of Breast Cancer Risk in the BCFR."  "" "26124344"]
     ["Changes in serum levels of miR-21, miR-210, and miR-373 in HER2-positive breast cancer patients undergoing neoadjuvant therapy: a translational research project within the Geparquinto trial."  "2014 Aug 3" "25086636"])

```

## License

Copyright © 2015 Insilico Informatics P/L.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
