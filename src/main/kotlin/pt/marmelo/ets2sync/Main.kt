package pt.marmelo.ets2sync

import pt.marmelo.ets2sync.parser.CfgParser
import pt.marmelo.ets2sync.parser.Context
import pt.marmelo.ets2sync.parser.ParseCallback
import pt.marmelo.ets2sync.parser.SiiTextParser
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.filechooser.FileSystemView.getFileSystemView
import javax.swing.filechooser.FileSystemView
import javax.swing.JFileChooser



fun main(args: Array<String>) {
    /*val fileContent = SiiFile().read(File("info_ets.sii"))
    Files.write(File("test.sii").toPath(), fileContent)
    println(String(fileContent))*/
    /*SiiTextParser.parse(fileContent) { context, name, value, sourceValue, offset ->
        //if (context.equals(Context.ATTRIBUTE)) {

            println("Context=$context name=$name value=$value sourceValue=$sourceValue offset=$offset")

        //}
    }*/

    //val save = Profile(Paths.get("Euro Truck Simulator 2/profiles/44616E69656C205B50545D"))
    /*val save = Profile(Paths.get("American Truck Simulator/profiles/44616E69656C205B50545D"))
    println("hello")*/

    /*println(Info.getDefaultDirectory(Game.ETS2))
    println(Info.getDefaultDirectory(Game.ATS))*/

    val contents = Files.readAllBytes(Paths.get("config.cfg"))
    CfgParser.parse(contents) { context, name, value, sourceValue, offset ->
        println("Context=$context name=$name value=$value sourceValue=$sourceValue offset=$offset")
    }

}
