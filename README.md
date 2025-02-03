<!-- The README-Template was borrowed from this repo: https://github.com/othneildrew/Best-README-Template -->

<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>
<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Don't forget to give the project a star!
*** Thanks again! Now go create something AMAZING! :D
-->


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Running</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#features">Features</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project
This is a menu-driven console application that allows users to perform a similarity search for the
inputted words and receive a text file with similar words.  Similarity search project comes with embeddings based on GloVE, with 50 values per word and 59,603 total entries.

The project was developed as part of the higher diploma course.


<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With
Java

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

### Prerequisites
Java SDK 22.0.1 is required to compile and run this project.


### Running

1. Clone the repo    
	```sh
   git clone https://github.com/github_username/repo_name.git
   ```
2. Go into the src/ folder
3. Open the console in the directory and compile it
   ```sh
   javac ie/atu/sw/ConsoleColour.java ie/atu/sw/Runner.java
   ```

4. Run it via a following command
   ```sh
   java ie/atu/sw/Runner
   ```

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

Once run, there will be a menu with 4 options:
1. will request to provide file path to the word embeddings.
2. will request to specify output location. Has a default option to output the file in a directory where the console was launched.
3. is a sub menu to start similarity search:
	Will only be triggered once previous options are completed.
	1. specify number of best similarity matches to output in a file (can be from 0 to 30,000)
	2. specify number of worst similarity matches to output in a file (can be 0 to 30,000)
	3. specify which similarity search algorithm to use: Cosine, Euclidean, or to use both.
	4. enter the text for similarity search. Can be one word or a string of words. Upon submission, it starts the search and creates an output file.
4. exit application

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- Features -->
## Features

- Informs and negates inappropriate input (chars instead of ints)
- Removes unnecessary input from search string (numbers, commas)
- System feedback (on loading embeddings and more)
- Support for both Cosine+Dot Search and Euclidean Searches.



<!-- CONTACT -->
## Contact

Mihass Konopelko - [@twitter_handle](https://x.com/MihassM60911) - konopelkomihass@gmail.com


<p align="right">(<a href="#readme-top">back to top</a>)</p>
