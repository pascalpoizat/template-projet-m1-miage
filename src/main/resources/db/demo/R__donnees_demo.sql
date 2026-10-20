-- Jeu de démonstration, chargé uniquement avec le profil « demo » (voir application-demo.properties).
-- Migration « répétable » (R__) et idempotente : relancer l'application ne duplique rien.
-- Remplacer ces données d'exemple par celles de vos features.
INSERT INTO note (titre, contenu)
SELECT 'Exemple', 'Une note pour essayer : le mot note revient, la note est courte, mais la note suffit.'
WHERE NOT EXISTS (SELECT 1 FROM note WHERE titre = 'Exemple');
