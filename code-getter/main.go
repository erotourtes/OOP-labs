package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"os/exec"
	"path/filepath"
	"strings"

	getfiles "github.com/erotourtes/oop-labs/code-getter/get-files"
)

const REPORT_PATH = "../report-tex/"

// const LATEX_FORMAT = "\\lstinputlisting[caption=%s, escapechar=, language=Kotlin]{%s}\n"
const LATEX_FORMAT = "\\lstinputlistingukr{%s}{%s}\n"
const SUBSECTION_FORMAT = "\\subsection{Module: %s}\n"

var EXCLUDE_FILES = []string{"styles.sty"}

func main() {
	labNum := readUserInput()
	copyReport(labNum)
	addCode(labNum)
	buildPdf(labNum, "main.tex")
}

func check(err error) {
	if err != nil {
		panic(err)
	}
}

func buildPdf(toLab string, texName string) {
	latexPath := filepath.Join(getReportPath(toLab), texName)
	cmd := fmt.Sprintf("pdflatex -output-directory %s %s", getReportPath(toLab), latexPath)

	_, err := exec.Command("bash", "-c", cmd).Output()
	if err != nil {
		fmt.Printf("%s", err)
	}

	fmt.Printf("Builded pdf")
}

func addCode(toLab string) {
	reportCopyPath := getReportPath(toLab)
	texPath := filepath.Join(reportCopyPath, "code.tex")

	texFile, err := os.Create(texPath)
	check(err)

	writer := bufio.NewWriter(texFile)
	defer writer.Flush()

	codeToPaste := pasteCode(toLab)
	writer.WriteString(strings.Join(codeToPaste, ""))
	fmt.Printf("Wrote %v numbers lines to the file %v\n", len(codeToPaste), texFile.Name())
}

func copyReport(toLab string) {
	reportSrc, err := os.ReadDir(REPORT_PATH)
	check(err)

	// Doesnt check if dir is exists
	reportDst := getReportPath(toLab)
	if _, err := os.Stat(reportDst); !os.IsNotExist(err) {
		fmt.Printf("Directory %v exists, skipping copying\n", reportDst)
		return
	}

  err = os.Mkdir(reportDst, os.ModePerm)
  if err != nil {
      println("Error creating directory, maybe lab dir is not exists")
      os.Exit(1)
  }

	println("Created report dir", reportDst)
	for _, srcEntry := range reportSrc {
    if (contains(EXCLUDE_FILES, srcEntry.Name())) {
      println("Skipping", srcEntry.Name())
      continue
    }
		src, err := os.Open(filepath.Join(REPORT_PATH, filepath.Base(srcEntry.Name())))
    defer src.Close()
		check(err)

		dst, err := os.Create(filepath.Join(reportDst, filepath.Base(src.Name())))
		check(err)
		defer dst.Close()

		_, err = io.Copy(dst, src)
		check(err)

		println("Copied to the", dst.Name())
	}
}

func contains(slice []string, value string) bool {
  for _, item := range slice {
    if item == value {
      return true
    }
  }
  return false
}

func pasteCode(toLab string) []string {
	files := getfiles.GetAllFiles(getLabPath(toLab))
	sorted := sortByPackage(files)

	output := []string{}

	for packageName, files := range sorted {
		output = append(output, fmt.Sprintf(SUBSECTION_FORMAT, packageName))
		for _, file := range files {
			output = append(output, fmt.Sprintf(LATEX_FORMAT, filepath.Base(file), file))
		}
		output = append(output, "\n")
	}

	changeToRelativePath(output, toLab)
	return output
}

func sortByPackage(files []string) map[string][]string {
	sortedFiles := make(map[string][]string)
	for _, file := range files {
		packageName := getPackageName(file)
		sortedFiles[packageName] = append(sortedFiles[packageName], file)
	}
	return sortedFiles
}

func getPackageName(file string) string {
	firstLine := strings.Split(getFirstLine(file), "\n")[0]
	return strings.Split(firstLine, " ")[1]
}

func getFirstLine(file string) string {
	openedFile, err := os.Open(file)
	check(err)
	defer openedFile.Close()

	scanner := bufio.NewScanner(openedFile)
	if scanner.Scan() {
		return scanner.Text()
	}

	panic("File is empty")
}

func readUserInput() string {
	var input string
	fmt.Print("Enter a lab number: ")
	fmt.Scanln(&input)
	return input
}

func getLabPath(labNumber string) string {
	return fmt.Sprintf("../lab%s/src", labNumber)
}

func getReportPath(toLab string) string {
	return filepath.Join(fmt.Sprintf("../lab%s", toLab), "report/")
}

func changeToRelativePath(files []string, toLab string) {
	prefixFolder := fmt.Sprintf("lab%s/", toLab)
	for i, file := range files {
		files[i] = strings.Replace(file, prefixFolder, "", 1)
	}
}
