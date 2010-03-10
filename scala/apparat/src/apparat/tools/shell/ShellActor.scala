/*
 * This file is part of Apparat.
 *
 * Apparat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Apparat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Apparat. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2009 Joa Ebert
 * http://www.joa-ebert.com/
 *
 */
package apparat.tools.shell

import actors.Actor._
import actors.Actor
import apparat.tools.stripper.Stripper
import apparat.tools.reducer.Reducer

/**
 * @author Joa Ebert
 */
class ShellActor extends Actor {
	def act() = loop {
		receive {
			case CommandEvent(command) => {
				val response = (command: Seq[Char]) match {
					case Seq('s','t','r','i','p','p','e','r',' ', rest @ _*) => {
						Stripper.main(createArguments(rest))
					}
					case Seq('r','e','d','u','c','e','r', ' ', rest @ _*) => {
						Reducer.main(createArguments(rest))
					}
					case Seq('h','e','l','p', ' ', rest @ _*) => (rest mkString "") match {
						case "reducer" => "reducer -i input [-o output] [-q quality]"
						case "stripper" => "stripper -i input [-o output]"
						case "help" | "exit" | "quit" | "stop" => "No detail help available."
						case other => "Error: Unknown command \"" + other + "\""
					}
					case Seq('h', 'e', 'l', 'p') => """help [command] - For detailed help
quit - Exit the Apparat shell
reducer - Convert lossless to lossy graphics
stripper - Strip traces and debug operations"""
					case _ => "Error: Unknown command \"" + command + "\""
				}

				sender ! response
			}
			case ExitEvent => exit()
		}
	}

	private def createArguments(arguments: Seq[Char]) = {
		//TODO split correct based on platform
		arguments mkString "" split " "
	}
}