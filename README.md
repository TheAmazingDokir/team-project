# Team Project

# Hirematch

Hirematch is a place where you can find a suitable candidate for the job or a perfect job placement is seconds.
Just create a profile, write catchy summary and dive into the pool of employers and jobseekers recommended for your inquiry.
Like and dislike suggest profiles, and if you and the person/company you are intersted in also likes you, can see them in matches.
There you find their full profile as well their contact info. So what are you waiting for go ahead and hit them up with  the offer.

User stories:
1. Users can register as job seekers or employers and create profiles (Milan)
2. Save/Edit user profile information to the database (ie. username, password ) (Eren)
3. Users can see recommended job postings/resumes based on if they are a job seeker or employer and interact with like dislike buttons (Fred)
4. Users can like or dislike recommendations, after that the next recommendation is fetched (Kyrylo)
5. Matches show up in each user’s matches page, and can be expanded to view more detailed information about other users (Wyatt)
6. When two users both like each other, a match is created between them and the matched profiles for a user can be fetched from the database (Maher)

Used APIs:
1. Pinecone (https://docs.pinecone.io/reference/java-sdk) - vector database provider. Allows to embed text and other datatypes as vectors, store them in the database and query for the nearest vector in the database and decode it back. We have successfully tried creating a new index (database) and calling the api with Java.
This api will be used to recommend users profiles matching either the employee they are looking for or job posting they are looking for based on the vector similarity between their profile and other users’ profiles.

2. MongoDB Atlas (https://www.mongodb.com/docs/atlas/atlas-ui/triggers/functions/api/) - a document oriented database system that allows to store data as a structured documents (similar to json objects). We have successfully tried creating new database and calling api with Java.
   We are going to use this api to store users, passwords, resumes, job postings etc. We won’t be performing similarity searches here - instead we will be using this to store and manipulate actual data.

