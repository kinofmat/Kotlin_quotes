/*
This program takes a sample text file as a data base of quotes. When running it, the user
can choose to receive a normal quote, or one that's mixed up from two different quotes.
Additionally the user can add an extra quote to the database.
*/

/*Just a couple of libraries needed for the program.
io is for the file I was using as the database.
random is for random numbers.*/
import java.io.File
import java.util.Random

//A couple of class variables. I used them in multiple functions.
var iterator: String? = "r"
val quotefile = File("quote_database.txt") //My file I used was titled quote_database. You may have to change that depending on what you choose to name your file.

    /*
    random will generate a random number each time it is called.
    The size of the random numbers is dependant on the getlength function,
    that determines how many quotes there are in the file.
    */
    fun random(): Int{
        val randall = Random()
        var rdmnbr = randall.nextInt(getlength())
        return rdmnbr
    }

    /*getlength will count the number of lines and then divide it by 3.
    For each quote there is a line for the actual quote, a line for the
    author, and a line of space between the next one.*/
    fun getlength(): Int{
        var i = 0
        quotefile.forEachLine{++ i} //forEachLine performs like a for loop through each line in the file.
        var nmbrqts = ((i/3)+1)
        return nmbrqts
    }

    //fromfile will grab a random quote from the file that I stored the quotes in.
    //It calls the random() function for a random number.
    fun fromfile(): String{
        var i = 0
        var rcvquote = ""
        var trial = random()

        quotefile.forEachLine{
            /* When is a bit like an if, else. In this one, I used it to add the quote to rcvquote, when
            it gets to the right line, and then the author as well from the next line. */
            when (trial*3){
                i -> rcvquote = it
                i - 1 -> rcvquote += " " + it
            }
            ++ i
        }
        return rcvquote
    }

    //All tofile will do is write the items to the file that's being used as the data base.
    fun tofile(quote: String, author: String){
        //Important that it appends the text so that it doesn't erase what was already there!
        quotefile.appendText("\n")
        quotefile.appendText(quote + "\n")
        quotefile.appendText(author +"\n")
    }

    /*addquote is a void function that neither takes any arguments or return anything.
    It will cal the tofile function and pass the information needed to write to the file.*/
    fun addquote(): Void?{
        var quote: String
        var author: String
        var check: String?

        //This loop will prompt the user for input for the quote and the author of that quote.
        //It needs to iterate through at least once, but if the user typed in something wrong it will allow them to go back through.
        do{
            println("Please enter what quote you would like to enter.\n For example: 'Moses supposes his toeses are roses.'")
            quote = readLine()!!
            println("Please enter who said this quote. (include a ~ before their name)\n For example: ~ Dolores Umbridge")
            author = readLine()!!
            println("We are about to add: " + quote + " " + author + " to the database." +
                    "\nIs this is the quote you want to add? ('Y'/'N')")
            check = readLine()
            check = check?.toLowerCase()
        } while (check == "n")
        tofile(quote, author) //Calls tofile so that the information can be saved to the file.
        println("You have sucessfully added " + quote + " " + author + " to our database")
        return null
    }

    /*
    This is actually a left over function from when I very first created this and hadn't implemented a database yet.
    fun quotes(): String {
        val quote= "Wipe yesterday away, and only worry about today, and the future won't go astray."
        return quote
    }*/

    /*intro is a simple function that is the starting text for the software. It will prompt the user for what they want to do.
    It is called many times, as after each task it will display the same message.*/
    fun intro(): String? {
        println(" You may choose one of several options.\n  Press 'r' to receive a random quote.\n  Press 'm' to receive" +
                " a quote mixed from several others.\n  Press 'a' to add a quote to the data base.\n  Press 'q' to quit")
        iterator = readLine()
        iterator = iterator?.toLowerCase()
        return iterator
    }

    //mixer is a bigger function because it takes parts from everything and combines two quotes together
    fun mixer(): String {
        var first = fromfile()
        var second = fromfile()

        /*This variation of when was really helpful. I was able to check if the quote for both first
        and second happened to be the same one. If they were then I would get a new quote for the second
        one, because no one would like a quote that they were expecting to be a weird one but get a real
        one instead.*/
        second = when(first){
            second -> fromfile()
            else -> second
        }

        var tophalf = (first.length)/2
        var bottomhalf = (second.length)/2
        val attribution = '~'
        var noattribution = second.split(attribution) //This will take off who the quote is by as fromfile returns a concated sting with this information.
        second = noattribution[0]

        /*These while loops will check that I'm not cutting the quote in the middle of a word. If it is, then
        it will add a single character at a time until it finds a space. This creates a second problem where
        some of the short quotes will include the closing ", so I had to add the if statement to check for it.*/
        while (first.get(tophalf) != ' ')
            ++tophalf
        if (first.get(tophalf-1) == '"')
            --tophalf
        while (second.takeLast(bottomhalf).get(0) != ' ')
            --bottomhalf

        var mixed = first.take(tophalf) + second.takeLast(bottomhalf) + "\n ~ Quoter"
        return mixed
    }

    /* We've reached the main function hooray! Being the main function this is the one that will run and through it's calls
    will travel through all the other functions. Through it's iterations, it can do any of the determined tasks for this program.*/
    fun main(){
        println("Welcome to the Quoter!")
        //Loop through as long as the user wants to see new quotes but allow them to stop once they are bored by it.
        while (iterator != "q"){
            iterator = intro()
            //This when acts like an if as well. It checks what was received by intro() to determine which function to call to perform the task.
            when (iterator) {
                "q" -> break //There is no need to go though any of the rest of the code as soon as the program determines the user wants to exit.
                "r" -> println(fromfile())
                "m" -> println(mixer())
                "a" -> addquote()
                else -> println("$iterator appears to not match one of the choices. Please try entering in the value again.")
            }
        }
    }