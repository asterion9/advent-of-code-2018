#Advent Of Code 2018

This project feature all solutions to the advent of code challenge of 2018.

## Usage

This is a maven sprig project using java 11.
Use `mvn clean install` to build the executable jar in `target` repository.

Launch with arguments `{day} {num} --cookie="{your_cookie}"`
where day is the day number on two digit (01 for day 1), num is the exercise you want (1 ou 2)
and your_cookie is the authentication cookie you use to automatically fetch your exercise input 
(in the form of `_ga=GA1.123456789123456789; session=81791b848a41b8148d14894e841f8148c41d81; _gid=GA1.2.123456789.123456789; _gat=1`).

Additionnally, day 13, 17 and 20 have a graphic representation using swing. 
To use it add `-Djava.awt.headless=false` to your argument list (solution may need to be edited to activate graphical printing).