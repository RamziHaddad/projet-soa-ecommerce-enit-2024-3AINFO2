namespace ProjetC_MVCSalleSport.Models
{
    public class Date
    {
        private int jour;
        private int mois;
        private int annee;

        public Date(int jour, int mois, int annee)
        {
            this.jour = jour;
            this.mois = mois;
            this.annee = annee;
        }

        public int Jour
        {
            get { return jour; }
            set { jour = value; }
        }

        public int Mois
        {
            get { return mois; }
            set { mois = value; }
        }

        public int Annee
        {
            get { return annee; }
            set { annee = value; }
        }

        public bool EstValide()
        {
            // Vérifie si la date est valide
            if (annee < 0)
                return false;

            if (mois < 1 || mois > 12)
                return false;

            int[] joursParMois = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
            int joursDansMois = joursParMois[mois];

            // Vérifie si l'année bissextile
            if (mois == 2 && EstBissextile(annee))
                joursDansMois = 29;

            return (jour >= 1 && jour <= joursDansMois);
        }

        private bool EstBissextile(int annee)
        {
            return (annee % 4 == 0 && (annee % 100 != 0 || annee % 400 == 0));
        }

        public override string ToString()
        {
            // Affiche la date sous forme de chaîne de caractères
            return $"{jour:D2}/{mois:D2}/{annee:D4}";
        }
    }
}

