# METTLE: a METmorphic Testing approach to assessing and validating unsupervised machine LEarning systems

## File structure

- **cluster**: clustering implementation
- **mr**: MR implmentation
- **util**: utils
- **evaluate**: clustering assessment (check violation & compute score)
- **main**: executive module
- **resources/testdata**: source dataset to be executed
- **lib**: auxiliary python script

## To run

in `main` directory:

- identify test cases (e.g., system under test, source dataset to be executed) in **MainTest.csv**
- running **MainTest.java** (identify MRs and relevant weights in terms of `MR preference` before running), MR compliance will be automatically generated as `evaluate.csv`
- identify weights in terms of `MR serverity` in `evaluate.csv` (after your further assessment), running **Evaluate.java** to acquire final ranking of investigated clustering systems


## Note

Python environments is addtionally required to support scientific computation (e.g., numpy, pandas, scikit-learn)
