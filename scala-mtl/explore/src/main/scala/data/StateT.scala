package scalaz
package data


final case class StateT[S, F[_], A](run: S => F[(S, A)])


object StateT extends StateTInstances with StateTFunctions
