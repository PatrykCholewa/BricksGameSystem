<h1> Program sędziowski do gry "Cegiełki" </h1>

<ol>
<h2> <li> Cel projektowy </h2>

Program został stworzony jako projekt w ramach przedmiotu
"__Algorytmy i struktury danych__" na __Politechnice Warszawskiej__.

Jednym z zadań w ramach tego przedmiotu było stworzenie programu walczącego, sztucznej
inteligencji, która miała konkrurować z innymi, stworzonymi przez kolegów z grupy dziełami
w ramach jakiejś wcześniej obranej gry. 

Głównym problemem jednak było przeprowadzenie
serii takich pojedynków, weryfikacja poprawności działań i podsumowywanie wyników.
W tym celu miał zostać zaprojektowany osobny program, który obsłuży wszystkie pozostałe
i pozwoli na obserwowanie przebiegu walk.

To są właśnie zadania stworzonego przez nas program - __Programu Sędzioweskiego__.

<h2> <li> Zasady gry </h2>

<ol>
<li>    
        </b>Plansza</b> na której toczy się rozgrywka to kwadrat n*n o n będącym liczbą
        nieparzystą niewiększą od 999.
        
<li>    
        <b>Plansza</b> to zbiór <b>komórek</b> które mają być później zapełnione przez graczy pdczas
        kolejnych ruchów.
<li>    
        Nie wszystkie <b>komórki</b> muszą być wstępnie puste. <b>Program Sędziowski</b> może
        (<i>na życzenie jego Operatora</i>) <b>planszę</b> niektóre <b>komórki</b> wypełnić.
<li>    
        Przykładowy wygląd planszy startowej:

     .01234. 
    0|X X X|
    1|  X  |
    2| X   |
    3|   X |
    4|_____|
       
<li>
        <b>Gracze</b> (<i>algorytmy walczące</i>) rozstawiają na przemian prostokąty 
        o wymiarach 2x1 pionowo lub poziomo na <b>planszy</b>.
<li>
        Rozgrywka kończy się gdy nie da się już postawić żadnego nowego prostokąta.
</ol>

<h2> <li> Protokół komunikacji Gracz-Sędzia </h2>

__Sędzia__ komunikuje się z __Graczami__ za pomocą ich standardowych wejść i wyjść 
( _stdin_ , _stdout_ ).

<ol>
<li>
        <b>Sędzia</b> przesyła pierwszemu <b>graczowi</b> informację o rozmiarze 
        <b>planszy</b>, a także o <b>komórkach</b> już zapełnionych przez system.
        np. <b>Plansza</b> rozmiaru 7x7 o zapełnionych <b>komórkach</b>
        o indeksach (2,3) i (4,5).
        
    7_2x3_4x5        
<li>
        <b>Gracz</b> przesyła informację zwrotną o przyjęciu komunikatu. Ma na to 1s.
        
    OK        
<li>
        <b>Sędzia</b> przesyła drugiemu <b>graczowi</b> tą samą informację.
        
    7_2x3_4x5
<li>
        Drugi <b>gracz</b> ma również sekundę na odpowiedź.
        
    OK        
<li>
        <b>Sędzia</b> przesyła pierwszemu <b>graczowi</b> informacje o rozpoczęciu gry.
        
    START
<li>
        <b>Gracz</b> ma 0.5s na odpowiedź jaki ruch zamierza wykonać.
        np. Wstawienie na <b>plaszę</b> prostokąta na <b>komórki</b>
        (1,2) , (2,2).
          
    1x2_2x2
<li>
        <b>Sędzia</b> wysyła drugiemu <b>graczowi</b> informację o ruchu poprzedniego.
        
    1x2_2x2
<li>
        <b>Gracz</b> ma 0.5s na wykonanie swojego ruchu.
       
    3x0_3x1
<li>
        <b>Sędzia</b> przesyła pierwszemu <b>graczowi</b> informację o ruchu poprzedniego.
        
    3x0_3x1
<li>
        itd. aż któryś z graczy spełni <b>warunki przegranej</b>.
        				
</ol>

<h2> <li> Warunki przegranej </h2>

<ol>
<li>
        Przekroczenie dozwolonego czasu przez <b>gracza</b> powoduje
        jego automatyczną przegraną.
<li>
        <b>Gracz</b>, który wykonał ruch nieprzewidziany w zasadach
        automatycznie przegrawa.
<li>
        Jeżeli gra zakończyła się w sposób przewidziany w <b>Regulaminie</b>,
        przegrywa gracz, który miałby wykonywać następny ruch (<i>czyli ten,
        który nie może już wykonać ruchu</i>).
</ol>

<br> 
Po zakończonej rozgrywce <b>Sędzia</b> wysyła: 
    
    STOP
    
do wszystkich <b>graczy</b> (<i><b>Program Gracz</b> powinien się zakończyć,
ale <b>Sędzia</b> profilaktycznie dodatkowo zabija proces.</i>).

<h2> <li> Dodatkowe założenia </h2> 
 
<ol>
<li>
    To <b>sędzia</b> uruchamia <b>graczy</b>.
<li>
    <b>Sędzia</b> będzie posiadał specjalny folder z podfolderami
    przeznaczonymi na pliki graczy. Takie pliki będą przede wszystkim dwa.
    Sam program i plik konfiguracyjny info.txt
    
    (komenda uruchomieniowa)
    (nick gracza)
<li>    
    <b>Sędzia</b> musi się uruchamiać na Windowsie.
<li>
    Musi być możliwość ręcznego przejrzenia przebiegu rozgrywki
    (np. wywołaie kolejnego ruchu).
<li>
    Musi być możliwość ręcznego ustawiania rozmiarów <b>planszy</b>.
<li>
    <b>Sędzia</b> musi mieć możliwość przeprowadzenia rozgrywki automatycznie.
<li>
    Musi być możliwość przejrzenia historii przebiegu rozgrywki. 
<li>
    <b>Sędzia</b> musi umieć automatycznie przeprowadzić turniej
    każdy z każdym (po dwie gry, raz jeden raz drugi)
    liczy kto ile ma zwycięstw, kto ile porażek)
<li>
    Program sędziowski ma opcję wybrania ręcznego początkowych
    zajętych komórek lub je losuje.
</ol>

</ol>

