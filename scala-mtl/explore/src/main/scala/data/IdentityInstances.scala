package scalaz
package data


import typeclass.MonadBaseClass


trait IdentityInstances {

  implicit val monadBase: MonadBase[Identity, Identity] =
      new MonadBaseClass.Endo[Identity] {
        def map[A, B](fa: Identity[A])(f: A => B): Identity[B] =
          Identity(f(fa.run))
        def ap[A, B]
            (fa: Identity[A])(f: Identity[A => B]): Identity[B] =
          Identity(f.run.apply(fa.run))
        def pure[A](a: A): Identity[A] = Identity(a)
        def flatMap[A, B]
            (oa: Identity[A])(f: A => Identity[B]): Identity[B] = f(oa.run)
        def flatten[A](ma: Identity[Identity[A]]): Identity[A] = ma.run
      }

}
